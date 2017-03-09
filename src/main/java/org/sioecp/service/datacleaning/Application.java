package org.sioecp.service.datacleaning;

import org.wso2.msf4j.MicroservicesRunner;

public class Application {
    // Default Port
    static int port = 8080;
    // Default config file
    static String configFile = "config.properties";
    // Default Max Rows to clean
    static int maxRowToClean = 1000;

    public static void main(String[] args) {

        // Handle parameters
        // Call should be -> java -jar Cleaning-Service.jar [-conf <propertiesFile>] [-port <port>] [-maxrow <maxrowtoclean>]
        for (int i = 0; i < args.length-1;i+=2){
            if (args[i].equals("-port"))
                port = Integer.parseInt(args[i+1]);
            else if (args[i].equals("-conf"))
                configFile = args[i+1];
            else if (args[i].equals("-maxrow"))
                maxRowToClean = Integer.parseInt(args[i+1]);
        }

        // Run microservice
        new MicroservicesRunner(port)
                .deploy(new DataCleaningService(configFile, maxRowToClean))
                .start();

    }
}