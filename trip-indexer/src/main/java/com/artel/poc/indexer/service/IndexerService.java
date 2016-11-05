package com.artel.poc.indexer.service;

import com.artel.poc.indexer.service.bean.Result;
import com.artel.poc.indexer.service.bean.TripJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndexerService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthService authService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TripCreatorService tripCreatorService;

    @Autowired
    private TripFetcherService tripFetcherService;

    @Autowired
    private TripBulkService tripBulkService;

    public void startIndexation(long fromId, long toId) {
        Map<String, Long> times = new LinkedHashMap<>();

        // Login
        long start = System.currentTimeMillis();
        List<String> cookies = authService.login();
        times.put("login", diff(start));

        // Fetch
        start = System.currentTimeMillis();
        List<List<Result>> result = tripFetcherService.fetch(fromId, toId, cookies);
        times.put("fetch", diff(start));

        // Transform
        start = System.currentTimeMillis();
        List<TripJson> tripsJson = result.stream()
                .map(tripFields -> tripCreatorService.createTripJson(tripFields))
                .collect(Collectors.toList());
        times.put("transform", diff(start));

        // Bulk
        start = System.currentTimeMillis();
        tripBulkService.bulkToEs(tripsJson);
        times.put("bulk", diff(start));

        // Logout
        start = System.currentTimeMillis();
        authService.logout(cookies);
        times.put("logout", diff(start));

        logger.debug("Timer: {}", times);
    }

    private long diff(long start) {
        return System.currentTimeMillis() - start;
    }

}
