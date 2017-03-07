package org.sioecp.service.datacleaning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sioecp.service.datacleaning.tools.SqlConnector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Weather Data Cleaning")
class DataCleaningServiceTest {

    static final String CONFIG_FILE_PATH = "test/config-test.properties";

    @Test
    void TestWeatherCleaning(){
        SqlConnector sql = new SqlConnector();
        sql.importPropertiesFromFile(CONFIG_FILE_PATH);

        // Count Data lake weather rows
        int lakeWeatherRows = sql.execCount("weather",null);

        // Count DW_weather rows
        int warehouseWeatherRows = sql.execCount("DW_weather",null);
        assertEquals(0,warehouseWeatherRows);

        // Exec cleaning service
        DataCleaningService cleaner = new DataCleaningService(CONFIG_FILE_PATH);
        cleaner.cleanWeather();

        // Count DW_weather rows
        warehouseWeatherRows = sql.execCount("DW_weather",null);
        assertEquals(3,warehouseWeatherRows);

        // Exec cleaning service again: nothing more should be cleaned
        cleaner = new DataCleaningService(CONFIG_FILE_PATH);
        cleaner.cleanWeather();

        // Count DW_weather rows
        warehouseWeatherRows = sql.execCount("DW_weather",null);
        assertEquals(3,warehouseWeatherRows);

        // Ensure Data lake weather rows werent touched
        int lakeWeatherRows2 = sql.execCount("weather",null);
        assertEquals(lakeWeatherRows,lakeWeatherRows2);
    }
}