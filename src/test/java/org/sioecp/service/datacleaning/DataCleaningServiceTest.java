package org.sioecp.service.datacleaning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sioecp.service.datacleaning.tools.SqlConnector;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Weather Data Cleaning")
class DataCleaningServiceTest {

    @Test
    void TestWeatherCleaning(){
        SqlConnector sql = new SqlConnector();
        sql.importPropertiesFromFile("config.properties");

        DataCleaningService cleaner = new DataCleaningService("config.properties");
        cleaner.cleanWeather();
    }
}