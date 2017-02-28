package org.sioecp.service.datacleaning.engine;

import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marin on 21/02/2017.
 */
public class WeatherDataCleaner {

    private static final int MAXROWS = 1000;
    private SqlConnector dbconnector;
    public int cleanedRows = 0;
    public int lastCleanedRow = -1;

    public WeatherDataCleaner(SqlConnector sql){
        dbconnector = sql;
    }

    public boolean runCleaning(){
        // Get last cleaned row number
        int firstRowToClean = getLastCleanedRow()+1;

        // Get last added row number
        int lastAddedRow = getLastDLRow();

        // Set max row to clean
        int maxRow = Math.min(firstRowToClean+MAXROWS,lastAddedRow);

        // Retrieve Cities list
        Map<String, Integer> cities = getCitiesList();
        if (cities.size() < 1)
            return false;

        // Fill DW_weather from Data Lake Weather data
        for (Map.Entry<String, Integer> city : cities.entrySet()){
            fillCleanedTable(firstRowToClean, maxRow, city.getValue(), city.getKey());
        }

        // Set last cleaned row
        setLastCleanedRow(maxRow);

        // Update counter
        cleanedRows = Math.max(maxRow - firstRowToClean, 0);
        lastCleanedRow = maxRow;

        return true;
    }

    // Copies Data Lake weather table into DataWarehouse table, removes duplicates and separate city from it
    private void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name){
        dbconnector.execWrite("INSERT INTO DW_weather (id, city_id, weather_group, pressure, " +
                "humidity_percentage, temperature, min_temperature, " +
                "max_temperature, wind_speed, wind_direction, cloudiness_percentage, rain_quantity, " +
                "snow_quantity, sun_set, sun_rise, calculation_time) " +
                "SELECT DISTINCT NULL, "+id_city+", weather.weather_group, weather.pressure, weather.humidity_percentage, " +
                "  weather.temperature, weather.min_temperature, weather.max_temperature, weather.wind_speed, " +
                "  weather.wind_direction, weather.cloudiness_percentage, weather.rain_quantity, " +
                "weather.snow_quantity, weather.sun_set, weather.sun_rise, weather.calculation_time " +
                "FROM weather " +
                "WHERE id_weather >= "+firstRow+" AND id_weather <= "+lastRow+" AND city_name='"+city_name+"'");
    }

    private void setLastCleanedRow(int lastRow){
        dbconnector.execWrite("UPDATE MS_DataCleaning_conf SET value='"+lastRow+"' " +
                "WHERE name='weather_last_cleaned_row'");
    }

    private Map<String,Integer> getCitiesList(){
        List<List<String>> res = dbconnector.execRead("SELECT id,name FROM DW_city ");
        HashMap<String,Integer> cities = new HashMap<>();
        for (List<String> city : res) {
            cities.put(city.get(1),Integer.parseInt(city.get(0)));
        }
        return cities;
    }

    private int getCityId(String city){
        List<String> res = dbconnector.execRead("SELECT id FROM DW_city " +
                "WHERE name = '"+city+"'").get(0);
        return Integer.parseInt(res.get(0));
    }

    // Returns the latest row number added to the Data Lake
    private int getLastDLRow() {
        List<String> res = dbconnector.execRead("SELECT id_weather FROM weather " +
                "ORDER BY id_weather DESC " +
                "LIMIT 1").get(0);
        return Integer.parseInt(res.get(0));
    }

    // Returns the latest cleaned row from the configuration table
    private int getLastCleanedRow() {
        List<String> res = dbconnector.execRead("SELECT value FROM MS_DataCleaning_conf " +
                "WHERE name='weather_last_cleaned_row'").get(0);
        return Integer.parseInt(res.get(0));
    }

}
