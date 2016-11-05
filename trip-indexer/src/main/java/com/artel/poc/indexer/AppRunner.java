package com.artel.poc.indexer;

import com.artel.poc.indexer.service.IndexerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;

@Component
public class AppRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IndexerService indexerService;

    private static final long STEP = 200;

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        logger.info("Started at {}", start);

        for (int i = 0; i < 100; i++) {
            long begin = i * STEP;
            long end = (i+1) * STEP;

            logger.debug("[{} - {}]", begin, end);
            indexerService.startIndexation(begin, end);
        }


//        // Kick of multiple, asynchronous lookups
//        Future<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
//        Future<User> page2 = gitHubLookupService.findUser("CloudFoundry");
//        Future<User> page3 = gitHubLookupService.findUser("Spring-Projects");
//
//        // Wait until they are all done
//        while (!(page1.isDone() && page2.isDone() && page3.isDone())) {
//            Thread.sleep(10); //10-millisecond pause between each check
//        }
//
//        // Print results, including elapsed time
//        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
//        logger.info("--> " + page1.get());
//        logger.info("--> " + page2.get());
//        logger.info("--> " + page3.get());
    }

}
