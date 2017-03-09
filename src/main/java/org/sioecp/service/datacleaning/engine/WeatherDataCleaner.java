package org.sioecp.service.datacleaning.engine;

import org.sioecp.service.datacleaning.tools.SqlConnector;


import java.util.List;



public class WeatherDataCleaner extends DataCleaner {


    public WeatherDataCleaner(SqlConnector sql){
        dbconnector = sql;
    }


    // Copies Data Lake weather table into DataWarehouse table, removes duplicates and separate city from it
    protected void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name){
        dbconnector.execWrite("INSERT INTO DW_weather (id, city_id, weather_group, pressure, " +
                "humidity_percentage, temperature, min_temperature, " +
                "max_temperature, wind_speed, wind_direction, cloudiness_percentage, rain_quantity, " +
                "snow_quantity, sun_set, sun_rise, calculation_time) " +
                "SELECT DISTINCT NULL, "+id_city+", weather.weather_group, weather.pressure, weather.humidity_percentage, " +
                "  weather.temperature, weather.min_temperature, weather.max_temperature, weather.wind_speed, " +
                "  weather.wind_direction, weather.cloudiness_percentage, weather.rain_quantity, " +
                "weather.snow_quantity, weather.sun_set, weather.sun_rise, weather.calculation_time " +
                "FROM weather " +
                "WHERE id_weather >= "+firstRow+" AND id_weather < "+lastRow+" AND city_name='"+city_name+"'");
    }

    protected void setLastCleanedRow(int lastRow){
        dbconnector.execWrite("UPDATE MS_DataCleaning_conf SET value='"+lastRow+"' " +
                "WHERE name='weather_last_cleaned_row'");
    }



    // Returns the latest row number added to the Data Lake
    protected int getLastDLRow() {
        List<String> res = dbconnector.execRead("SELECT id_weather FROM weather " +
                "ORDER BY id_weather DESC " +
                "LIMIT 1").get(0);
        return Integer.parseInt(res.get(0));
    }

    // Returns the latest cleaned row from the configuration table
    protected int getLastCleanedRow() {
        List<String> res = dbconnector.execRead("SELECT value FROM MS_DataCleaning_conf " +
                "WHERE name='weather_last_cleaned_row'").get(0);
        return Integer.parseInt(res.get(0));
    }

}
