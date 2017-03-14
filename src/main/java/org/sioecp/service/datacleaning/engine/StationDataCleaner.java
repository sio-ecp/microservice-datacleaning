package org.sioecp.service.datacleaning.engine;

import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.List;


public class StationDataCleaner extends DataCleaner {



    public StationDataCleaner(SqlConnector sql){
        dbconnector = sql;
    }


    // Copies Data Lake station table into DataWarehouse table, removes duplicates and separate city from it
    protected void fillCleanedTable(int firstRow, int lastRow, int id_city, String city_name){
        dbconnector.execWrite("INSERT INTO DW_station(id,station_number,city_id,station_name,address,banking,bonus,latitude,longitude,elevation)" +
                " SELECT DISTINCT NULL, station.station_number,"+id_city+",station.station_name,station.address,station.banking," +
                " station.bonus, station.latitude, station.longitude, stationelevation.elevation" +
                " FROM station join stationelevation on station.station_number=stationelevation.station_number and station.contract_name=stationelevation.contract_name" +
                " WHERE station.id_station >= "+firstRow+" AND station.id_station < "+lastRow+" AND station.contract_name='"+city_name+"'"+
                " and not exists (select 1 from DW_station where station_number=station.station_number)");
    }


    protected void setLastCleanedRow(int lastRow){
        dbconnector.execWrite("UPDATE MS_DataCleaning_conf SET value='"+lastRow+"' " +
                "WHERE name='station_last_cleaned_row'");
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
                "WHERE name='station_last_cleaned_row'").get(0);
        return Integer.parseInt(res.get(0));
    }


}
