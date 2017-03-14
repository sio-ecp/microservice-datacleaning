package org.sioecp.service.datacleaning.engine;

import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.List;


public class StationStateDataCleaner extends DataCleaner {



    public StationStateDataCleaner(SqlConnector sql){
        dbconnector = sql;
    }


    // Copies Data Lake station table into DataWarehouse table, removes duplicates and separate city from it
    protected void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name){
        dbconnector.execWrite("INSERT INTO DW_station_state(id,id_station,status,operational_bike_stands,available_bike_stands,"+
                "available_bikes,last_update,movements)"+
                " SELECT DISTINCT NULL, DW_station.id,station.status,station.operational_bike_stands," +
                " station.available_bike_stands, station.available_bikes, station.last_update, NULL"+
                " FROM station,DW_station" +
                " WHERE station.id_station >= "+firstRow+" AND station.id_station < "+lastRow+
                " and station.station_number=DW_station.station_number");
    }


    protected void setLastCleanedRow(int lastRow){
        dbconnector.execWrite("UPDATE MS_DataCleaning_conf SET value='"+lastRow+"' " +
                "WHERE name='station_state_last_cleaned_row'");
    }



    // Returns the latest row number added to the Data Lake
    protected int getLastDLRow() {
        List<String> res = dbconnector.execRead("SELECT id_station FROM station " +
                "ORDER BY id_station DESC " +
                "LIMIT 1").get(0);
        return Integer.parseInt(res.get(0));
    }

    // Returns the latest cleaned row from the configuration table
    protected int getLastCleanedRow() {
        List<String> res = dbconnector.execRead("SELECT value FROM MS_DataCleaning_conf " +
                "WHERE name='station_state_last_cleaned_row'").get(0);
        return Integer.parseInt(res.get(0));
    }


}
