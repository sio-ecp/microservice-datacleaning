package org.sioecp.service.datacleaning.engine;

import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marwa on 28/02/2017.
 */
public class StationDataCleaner {

    private static int MAXROWS = 1000;
    private SqlConnector dbconnector;
    public int cleanedRows = 0;
    public int lastCleanedRow = -1;

    public StationDataCleaner(SqlConnector sql){
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

        // Fill DW_station from Data Lake station data
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

    // Copies Data Lake station table into DataWarehouse table, removes duplicates and separate city from it
    private void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name){
        dbconnector.execWrite("INSERT INTO DW_station(id,station_number,city_id,station_name,address,"+
                "banking,bonus,latitude,longitude,elevation)"+
                " SELECT DISTINCT NULL, Station.station_number,"+id_city+",Station.station_name,Station.address," +
                " Station.banking, Station.bonus, Station.latitude, Station.longitude,StationElevation.elevation"+
                " FROM Station,StationElevation" +
                " WHERE Station.id_station >= "+firstRow+" AND Station.id_station <= "+lastRow+" AND city_name='"+city_name+"'" +
                " and Station.station_number=StationElevation.station_number and Station.contract_name=StationElevation.contract_name");
    }


    private void setLastCleanedRow(int lastRow){
        dbconnector.execWrite("UPDATE MS_DataCleaning_conf SET value='"+lastRow+"' " +
                "WHERE name='station_last_cleaned_row'");
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
        List<String> res = dbconnector.execRead("SELECT id_station FROM Station " +
                "ORDER BY id_station DESC " +
                "LIMIT 1").get(0);
        return Integer.parseInt(res.get(0));
    }

    // Returns the latest cleaned row from the configuration table
    private int getLastCleanedRow() {
        List<String> res = dbconnector.execRead("SELECT value FROM MS_DataCleaning_conf " +
                "WHERE name='station_last_cleaned_row'").get(0);
        return Integer.parseInt(res.get(0));
    }


}
