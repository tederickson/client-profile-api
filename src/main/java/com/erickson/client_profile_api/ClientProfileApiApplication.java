package com.erickson.client_profile_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableRetry
public class ClientProfileApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientProfileApiApplication.class, args);
    }
}
