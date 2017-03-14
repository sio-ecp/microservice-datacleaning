package org.sioecp.service.datacleaning;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Weather Data Cleaning")
class DataCleaningServiceTest {

    private static final String CONFIG_FILE_PATH = "test/config-test.properties";
    private static SqlConnector sql;
    private static DataCleaningService cleaner;

    @BeforeAll
    static void initSql(){
        sql = new SqlConnector();
        sql.importPropertiesFromFile(CONFIG_FILE_PATH);
        cleaner = new DataCleaningService(CONFIG_FILE_PATH,1000);
    }

    /*@Test
    void TestWeatherCleaning(){

        // Count Data lake weather rows
        int lakeWeatherRows = sql.execCount("weather",null);

        // Count DW_weather rows
        int warehouseWeatherRows = sql.execCount("DW_weather",null);
        assertEquals(0,warehouseWeatherRows);

        // Exec cleaning service
        cleaner.cleanWeather();

        // Count DW_weather rows
        warehouseWeatherRows = sql.execCount("DW_weather",null);
        assertEquals(3,warehouseWeatherRows);

        // Exec cleaning service again: nothing more should be cleaned
        cleaner.cleanWeather();

        // Count DW_weather rows
        warehouseWeatherRows = sql.execCount("DW_weather",null);
        assertEquals(3,warehouseWeatherRows);

        // Ensure Data lake weather rows werent touched
        int lakeWeatherRows2 = sql.execCount("weather",null);
        assertEquals(lakeWeatherRows,lakeWeatherRows2);
    }*/

    @Test
    void TestStationCleaning(){

        // Count Data lake station and StationElevation rows
        int lakeStationRows = sql.execCount("station",null);
        int lakeStationElevationRows = sql.execCount("stationelevation",null);

        // Count DW_Station and StationState rows
        int warehouseStationRows = sql.execCount("DW_station",null);
        int warehouseStationStateRows = sql.execCount("DW_station_state",null);
        assertEquals(0,warehouseStationRows);
        assertEquals(0,warehouseStationStateRows);

        // Exec cleaning service
        cleaner.cleanStation();
        cleaner.cleanStationState();

        // Count DW_Station rows
        warehouseStationRows = sql.execCount("DW_station",null);
        warehouseStationStateRows = sql.execCount("DW_station_state",null);

        // Exec cleaning service again: nothing more should be cleaned
        cleaner.cleanStation();
        cleaner.cleanStationState();

        // Count DW_station rows
        int warehouseStationRows_1 = sql.execCount("DW_station",null);
        int warehouseStationStateRows_1 = sql.execCount("DW_station_state",null);
        assertEquals(warehouseStationRows,warehouseStationRows_1);
        assertEquals(warehouseStationStateRows,warehouseStationStateRows_1);

        // Ensure Data lake station rows werent touched
        int lakeStationRows_1 = sql.execCount("station",null);
        int lakeStationElevationRows_1 = sql.execCount("stationelevation",null);
        assertEquals(lakeStationRows,lakeStationRows_1);
        assertEquals(lakeStationElevationRows,lakeStationElevationRows_1);
    }
}