package org.sioecp.service;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/clean")
public class DataCleaningService {

    @GET
    @Path("/weather")
    public String cleanWeather() {

        // Actions to perform for WEATHER
        // Clean duplicate rows
        // Add city if no exist (<< or other behaviour? How to match cities automatically?)
        //

        return "Clean OK";
    }

    @GET
    @Path("/station")
    public String cleanStation() {

        // Actions to perform for STATION
        // Clean duplicate rows
        // Add city if no exist (<< or other behaviour?)
        // Add station if no exist
        // Process movements (<< here is more efficient, but not really part of cleaning...)


        return "Clean OK";
    }

}