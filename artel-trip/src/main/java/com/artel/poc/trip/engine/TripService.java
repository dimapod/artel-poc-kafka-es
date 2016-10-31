package com.artel.poc.trip.engine;

import com.artel.poc.trip.Trip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TripService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void saveTrip(Trip trip) {
        try {
            String str = objectMapper.writeValueAsString(trip);
            kafkaTemplate.send("trips", trip.getTripId(), str);
        } catch (JsonProcessingException e) {
            logger.error("Cannot serialize trip {}", trip);
        }
    }

}
