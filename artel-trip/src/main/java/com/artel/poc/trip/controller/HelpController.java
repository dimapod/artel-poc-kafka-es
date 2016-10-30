package com.artel.poc.trip.controller;

import com.artel.poc.trip.engine.bean.TripContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelpController {

    @Autowired
    private TripContainer tripContainer;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/help")
    @ResponseBody
    String help() throws JsonProcessingException {
        return objectMapper.writeValueAsString(tripContainer);
    }

}
