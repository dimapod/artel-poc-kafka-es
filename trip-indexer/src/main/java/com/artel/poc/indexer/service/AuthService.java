package com.artel.poc.indexer.service;

import com.artel.poc.indexer.service.bean.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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

    public AuthInfo login() {

        ResponseEntity<String> info = restTemplate.exchange(apiContext + "/info", HttpMethod.GET, new HttpEntity<>(""), String.class);

        List<String> cookies = info.getHeaders().get("Set-Cookie");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        if (cookies != null) {
            headers.put("Cookie", cookies);
        }
        getXSRF(cookies)
                .ifPresent(x -> headers.add("X-XSRF-TOKEN", x.substring(11, 47)));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user);
        map.add("password", password);
        ResponseEntity<String> exchange = restTemplate.exchange(apiContext + "/login", HttpMethod.POST, new HttpEntity<>(map, headers), String.class);
        return new AuthInfo(exchange.getHeaders().get("Set-Cookie"), getXSRF(cookies).orElse(null));
    }

    public void logout(AuthInfo authInfo) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.put("Cookie", authInfo.getCookies());
        restTemplate.exchange(apiContext + "/logout", HttpMethod.POST, new HttpEntity<>("", requestHeader), String.class);
    }

    private Optional<String> getXSRF(List<String> cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        return cookies.stream()
                .filter(c -> c.contains("XSRF-TOKEN"))
                .findFirst();
    }
}
