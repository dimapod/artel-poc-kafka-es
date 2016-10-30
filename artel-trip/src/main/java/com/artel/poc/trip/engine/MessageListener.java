package com.artel.poc.trip.engine;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

//    @KafkaListener(id = "foo", topicPartitions = {@TopicPartition(topic = "acq", partitions = {"0"})})
//    public void listen(String message) {
//        System.out.println(">>>>>>> " + message);
//    }
}
