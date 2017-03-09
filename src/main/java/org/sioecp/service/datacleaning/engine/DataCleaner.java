package org.sioecp.service.datacleaning.engine;


import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataCleaner {

    protected static int MAXROWS = 1000;
    protected SqlConnector dbconnector;
    public int cleanedRows = 0;
    public int lastCleanedRow = -1;

    public boolean runCleaning(){
        // Get last cleaned row number
        int firstRowToClean = getLastCleanedRow()+1;

        // Get last added row number
        int lastAddedRow = getLastDLRow();

        // Set max row to clean
        int maxRow = Math.min(firstRowToClean+MAXROWS,lastAddedRow+1);

        // Retrieve Cities list
        Map<String, Integer> cities = getCitiesList();
        if (cities.size() < 1)
            return false;

        // Fill DW_station from Data Lake station data
        for (Map.Entry<String, Integer> city : cities.entrySet()){
            fillCleanedTable(firstRowToClean, maxRow, city.getValue(), city.getKey());
        }

        // Update counter
        cleanedRows = Math.max(maxRow - firstRowToClean, 0);
        lastCleanedRow = maxRow-1;

        // Set last cleaned row
        setLastCleanedRow(lastCleanedRow);

        return true;
    }

    abstract void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name);

    abstract void setLastCleanedRow(int lastRow);

    abstract int getLastDLRow();

    abstract int getLastCleanedRow();

    protected Map<String,Integer> getCitiesList(){
        List<List<String>> res = dbconnector.execRead("SELECT id,name FROM DW_city ");
        HashMap<String,Integer> cities = new HashMap<>();
        for (List<String> city : res) {
            cities.put(city.get(1),Integer.parseInt(city.get(0)));
        }
        return cities;
    }

    protected int getCityId(String city){
        List<String> res = dbconnector.execRead("SELECT id FROM DW_city " +
                "WHERE name = '"+city+"'").get(0);
        return Integer.parseInt(res.get(0));
    }

}
