package org.sioecp.service.datacleaning;

import org.wso2.msf4j.MicroservicesRunner;

public class Application {
    public static void main(String[] args) {

        if (args.length > 0 )
            new MicroservicesRunner()
                    .deploy(new DataCleaningService(args[0]))
                    .start();
        else
            new MicroservicesRunner()
                .deploy(new DataCleaningService())
                .start();
    }
}