package com.artel.poc.indexer.service;

import com.artel.poc.indexer.service.bean.AuthInfo;
import com.artel.poc.indexer.service.bean.Result;
import com.artel.poc.indexer.service.bean.TripJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class IndexerService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TripCreatorService tripCreatorService;

    @Autowired
    private TripFetcherService tripFetcherService;

    @Autowired
    private TripBulkService tripBulkService;

    @Async
    public Future<Integer> startIndexation(long fromId, long toId, AuthInfo authInfo) {
        Map<String, Long> times = new LinkedHashMap<>();
        logger.debug("Start task: [{} - {}]", fromId, toId);

        // Fetch
        long start = System.currentTimeMillis();
        List<List<Result>> result = tripFetcherService.fetch(fromId, toId, authInfo);
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

        logger.debug("... end task: [{} - {}] ({})", fromId, toId, times);

        return new AsyncResult<>(10);
    }

    private long diff(long start) {
        return System.currentTimeMillis() - start;
    }

}
