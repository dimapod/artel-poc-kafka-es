package com.artel.poc.acq.acquisition;

import com.artel.poc.acq.TripMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AcqService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void process(TripMessage tripMessage) throws JsonProcessingException {
        int partitionId = (int) tripMessage.getImei() % 2;
        kafkaTemplate.send("acq", partitionId, tripMessage.getImei(), objectMapper.writeValueAsString(tripMessage));
        logger.debug("Message sent to partition {}: {}", partitionId, tripMessage);
    }

}
