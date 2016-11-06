package com.artel.poc.indexer;

import com.artel.poc.indexer.service.MainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MainExecutor mainExecutor;

    private static final long START_ID = 0;
    private static final long END_ID = 40000;

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();

        logger.info("------------ Started ------------", start);
        mainExecutor.index(START_ID, END_ID);

        Thread.sleep(2000);
        logger.info("------------ Finished in {} ms ------------", (System.currentTimeMillis() - start));
    }

}
