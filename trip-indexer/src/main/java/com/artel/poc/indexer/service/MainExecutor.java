package com.artel.poc.indexer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class MainExecutor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long STEP = 500;
    private static final long THREAD_NB = 5;

    @Autowired
    private IndexerService indexerService;

    @Autowired
    private AuthService authService;

    public void index(long fromId, long toId) throws InterruptedException {
        long counter = fromId;
        List<Future<Integer>> tasks = new ArrayList<>();

        // Login
        List<String> cookies = authService.login();
        logger.debug("-LOGIN-");

        // Threads
        while (counter < toId) {
            while (tasks.size() < THREAD_NB) {
                long begin = counter;
                counter = counter + STEP;
                Future<Integer> task = indexerService.startIndexation(begin, counter, cookies);
                tasks.add(task);
                //tasks.add(indexerService.testAsync(begin, counter));
            }

            // Wait for at least 1 thread to complete, then remove it from tasks
            List<Future<Integer>> completed = new ArrayList<>();
            while (completed.isEmpty()) {
                completed = tasks.stream()
                        .filter(Future::isDone)
                        .collect(Collectors.toList());
                Thread.sleep(10);
            }
            tasks.removeAll(completed);
        }

        // Logout
        authService.logout(cookies);
        logger.debug("-LOGOUT-");
    }
}
