package com.artel.poc.indexer.service;

import com.artel.poc.indexer.service.bean.Result;
import com.artel.poc.indexer.service.bean.ResultAdminQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Collections.singletonList;

@Service
public class TripFetcherService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${telematics.api.url:}")
    private String context;

    private static final String QUERY = "{'query':'select * from trip where id >= %d and id < %d'}".replaceAll("'","\"");

    public List<List<Result>> fetch(long fromId, long toId, List<String> cookies) {
        String q = String.format(QUERY, fromId, toId);
        ResponseEntity<ResultAdminQuery> response = restTemplate.exchange(context + "/admin/query", HttpMethod.POST,
                new HttpEntity<>(q, getHttpHeaders(cookies)), ResultAdminQuery.class);
        return response.getBody().getResult();
    }

    private HttpHeaders getHttpHeaders(List<String> cookies) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.put("Cookie", cookies);
        requestHeader.put("Content-Type", singletonList("application/json"));
        return requestHeader;
    }
}
