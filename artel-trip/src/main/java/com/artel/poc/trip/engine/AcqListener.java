package com.artel.poc.trip.engine;

import com.artel.poc.acq.TripMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AcqListener implements AcknowledgingMessageListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TripEngine tripEngine;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Object data, Acknowledgment acknowledgment) {

        try {
            TripMessage tripMessage = parse((ConsumerRecord) data);
            System.out.println("Received : " + tripMessage);
            tripEngine.process(tripMessage, ((ConsumerRecord) data).offset(), acknowledgment);
        } catch (IOException e) {
            logger.error("Message can not be parsed", e);
        }
    }

    private TripMessage parse(ConsumerRecord record) throws IOException {
        return objectMapper.readValue(String.valueOf(record.value()), TripMessage.class);
    }
}
