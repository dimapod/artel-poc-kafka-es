package com.artel.poc.es.service;

import com.artel.poc.trip.Trip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EsIndexService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransportClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public void indexTrip(Trip trip) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(trip);

        IndexResponse response = client.prepareIndex("arval", "trip")
                .setSource(json)
                .get();

        logger.debug("Trip {} in ES: id={} / v={}", response.isCreated() ? "created" : "updated", response.getId(), response.getVersion());
    }

}
