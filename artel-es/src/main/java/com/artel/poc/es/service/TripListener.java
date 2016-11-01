package com.artel.poc.es.service;

import com.artel.poc.trip.Trip;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TripListener {
    @Autowired
    private EsIndexService esIndexService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(id = "foo", topics = "trips")
    public void listen(String message) throws IOException {
        System.out.println(">>>>>>> Trip : " + message);
        Trip trip = objectMapper.readValue(message, Trip.class);
        esIndexService.indexTrip(trip);
    }
}
