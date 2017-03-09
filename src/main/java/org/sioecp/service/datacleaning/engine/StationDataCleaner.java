package org.sioecp.service.datacleaning.engine;

import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.List;


public class StationDataCleaner extends DataCleaner {



    public StationDataCleaner(SqlConnector sql){
        dbconnector = sql;
    }


    // Copies Data Lake station table into DataWarehouse table, removes duplicates and separate city from it
    protected void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name){
        dbconnector.execWrite("INSERT INTO DW_station(id,station_number,city_id,station_name,address,"+
                "banking,bonus,latitude,longitude,elevation)"+
                " SELECT DISTINCT NULL, Station.station_number,"+id_city+",Station.station_name,Station.address," +
                " Station.banking, Station.bonus, Station.latitude, Station.longitude,StationElevation.elevation"+
                " FROM Station,StationElevation" +
                " WHERE Station.id_station >= "+firstRow+" AND Station.id_station < "+lastRow+" AND city_name='"+city_name+"'" +
                " and Station.station_number=StationElevation.station_number and Station.contract_name=StationElevation.contract_name");
    }


    protected void setLastCleanedRow(int lastRow){
        dbconnector.execWrite("UPDATE MS_DataCleaning_conf SET value='"+lastRow+"' " +
                "WHERE name='station_last_cleaned_row'");
    }



    // Returns the latest row number added to the Data Lake
    protected int getLastDLRow() {
        List<String> res = dbconnector.execRead("SELECT id_station FROM Station " +
                "ORDER BY id_station DESC " +
                "LIMIT 1").get(0);
        return Integer.parseInt(res.get(0));
    }

    // Returns the latest cleaned row from the configuration table
    protected int getLastCleanedRow() {
        List<String> res = dbconnector.execRead("SELECT value FROM MS_DataCleaning_conf " +
                "WHERE name='station_last_cleaned_row'").get(0);
        return Integer.parseInt(res.get(0));
    }


}
