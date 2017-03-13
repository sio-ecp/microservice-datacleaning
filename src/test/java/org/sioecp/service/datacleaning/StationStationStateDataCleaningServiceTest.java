package org.sioecp.service.datacleaning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sioecp.service.datacleaning.tools.SqlConnector;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test Station and StationState Data Cleaning")
class StationStationStateDataCleaningServiceTest {

    static final String CONFIG_FILE_PATH = "test/config-test.properties";

    @Test
    void TestStationCleaning(){
        SqlConnector sql = new SqlConnector();
        sql.importPropertiesFromFile(CONFIG_FILE_PATH);

        // Count Data lake station and StationElevation rows
        int lakeStationRows = sql.execCount("station",null);
        int lakeStationElevationRows = sql.execCount("StationElevation",null);

        // Count DW_Station and StationState rows
        int warehouseStationRows = sql.execCount("DW_station",null);
        int warehouseStationStateRows = sql.execCount("DW_station_state",null);
        assertEquals(0,warehouseStationRows);
        assertEquals(0,warehouseStationStateRows);

        // Exec cleaning service
        DataCleaningService cleaner = new DataCleaningService(CONFIG_FILE_PATH,1000);
        cleaner.cleanStation();
        cleaner.cleanStationState();

        // Count DW_Station rows
        warehouseStationRows = sql.execCount("DW_station",null);
        warehouseStationStateRows = sql.execCount("DW_station_state",null);
        // Exec cleaning service again: nothing more should be cleaned
        cleaner = new DataCleaningService(CONFIG_FILE_PATH,1000);
        cleaner.cleanStation();
        cleaner.cleanStationState();

        // Count DW_station rows
        int warehouseStationRows_1 = sql.execCount("DW_station",null);
        int warehouseStationStateRows_1 = sql.execCount("DW_station_state",null);
        assertEquals(warehouseStationRows,warehouseStationRows_1);
        assertEquals(warehouseStationStateRows,warehouseStationStateRows_1);

        // Ensure Data lake station rows werent touched
        int lakeStationRows_1 = sql.execCount("station",null);
        int lakeStationElevationRows_1 = sql.execCount("StationElevation",null);
        assertEquals(lakeStationRows,lakeStationRows_1);
        assertEquals(lakeStationElevationRows,lakeStationElevationRows_1);
    }
}