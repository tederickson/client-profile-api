package com.erickson.client_profile_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ClientProfileApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientProfileApiApplication.class, args);
    }

}
