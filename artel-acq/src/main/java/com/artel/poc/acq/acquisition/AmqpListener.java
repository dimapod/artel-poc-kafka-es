package com.artel.poc.acq.acquisition;

import com.artel.poc.acq.TripMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AmqpListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AcqService acqService;

    @Override
    public void onMessage(Message message) {
        String msg = new String(message.getBody());
        try {
            TripMessage tripMessage = objectMapper.readValue(msg, TripMessage.class);
            acqService.process(tripMessage);
        } catch (IOException e) {
            logger.error("Cannot parse message: " + msg, e);
        } catch (Exception e) {
            logger.error("Error: " + msg, e);
        }
    }


//    Can listen like this
//    @RabbitListener(queues = "test")
//    public void onMessage(Message message) {
//        System.out.printf("Message: %s", new String(message.getBody()));
//    }
}