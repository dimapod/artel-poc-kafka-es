package com.artel.poc.indexer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${telematics.api.user:}")
    private String user;

    @Value("${telematics.api.password:}")
    private String password;

    @Value("${telematics.api.url:}")
    private String apiContext;

    public List<String> login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user);
        map.add("password", password);
        ResponseEntity<String> exchange = restTemplate.exchange(apiContext + "/login", HttpMethod.POST, new HttpEntity<>(map, headers), String.class);
        return exchange.getHeaders().get("Set-Cookie");
    }

    public void logout(List<String> cookie) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.put("Cookie", cookie);
        restTemplate.exchange(apiContext + "/logout", HttpMethod.POST, new HttpEntity<>("", requestHeader), String.class);
    }
}
