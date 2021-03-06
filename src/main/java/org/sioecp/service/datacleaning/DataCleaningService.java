package org.sioecp.service.datacleaning;

import org.sioecp.service.datacleaning.engine.DataCleaner;
import org.sioecp.service.datacleaning.engine.StationDataCleaner;
import org.sioecp.service.datacleaning.engine.StationStateDataCleaner;
import org.sioecp.service.datacleaning.engine.WeatherDataCleaner;
import org.sioecp.service.datacleaning.tools.SqlConnector;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/clean")
public class DataCleaningService {

    private String propertiesPath;

    public DataCleaningService(String propertiesPath, int maxRowToClean){
        this.propertiesPath = propertiesPath;
        DataCleaner.MAXROWS = maxRowToClean;
    }

    @GET
    @Path("/weather")
    public String cleanWeather() {

        // Actions to perform for WEATHER

        // Setup SQL connection
        SqlConnector sql = new SqlConnector();
        sql.importPropertiesFromFile(propertiesPath);

        // Init cleaner class
        WeatherDataCleaner cleaner = new WeatherDataCleaner(sql);

        // Start cleaning
        boolean state = cleaner.runCleaning();

        if (state)
            return "{status:'OK',cleanedRows:"+cleaner.cleanedRows+",lastCleanedRow:"+cleaner.lastCleanedRow+"}";
        else
            return "{status:'FAILED'}";
    }

    @GET
    @Path("/stations")
    public String cleanStation() {

        // Setup SQL connection
        SqlConnector sql = new SqlConnector();
        sql.importPropertiesFromFile(propertiesPath);

        // Init cleaner class
        StationDataCleaner cleaner = new StationDataCleaner(sql);

        // Start cleaning
        boolean state = cleaner.runCleaning();

        if (state)
            return "{status:'OK',cleanedRows:"+cleaner.cleanedRows+",lastCleanedRow:"+cleaner.lastCleanedRow+"}";
        else
            return "{status:'FAILED'}";

    }


    @GET
    @Path("/stationState")
    public String cleanStationState() {

        // Setup SQL connection
        SqlConnector sql = new SqlConnector();
        sql.importPropertiesFromFile(propertiesPath);

        // Init cleaner class
        StationStateDataCleaner cleaner = new StationStateDataCleaner(sql);

        // Start cleaning
        boolean state = cleaner.runCleaning();

        if (state)
            return "{status:'OK',cleanedRows:"+cleaner.cleanedRows+",lastCleanedRow:"+cleaner.lastCleanedRow+"}";
        else
            return "{status:'FAILED'}";

    }

}