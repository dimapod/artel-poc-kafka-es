package com.artel.poc.acq.acquisition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class TryController {

    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;

    @RequestMapping("/")
    @ResponseBody
    String home() {

        kafkaTemplate.send("multibroker", "Hello from spring boot " + new Date().toString());

        return "Published";
    }

}
