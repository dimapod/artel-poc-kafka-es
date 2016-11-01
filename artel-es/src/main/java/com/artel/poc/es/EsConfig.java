package com.artel.poc.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableKafka
@PropertySource("classpath:application.properties")
public class EsConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Value("${es.broker:localhost:9092}")
//    private String kafkaBroker;
//
//    @Value("${kafka.group:kafka-trips}")
//    private String group;
//
//    @Value("${kafka.topic:trips}")
//    private String topic;

    @Bean
    TransportClient transportClient() throws UnknownHostException {
        return TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }


}
