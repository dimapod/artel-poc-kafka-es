package com.artel.poc.acq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableAutoConfiguration
public class AcqApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AcqApplication.class, args);
    }

}
