package com.erickson.client_profile_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "asyncTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);          // Minimum number of threads in the pool
        executor.setMaxPoolSize(2);          // Maximum number of threads in the pool
        executor.setQueueCapacity(20);       // Maximum tasks that can be queued
        executor.setThreadNamePrefix("Erickson-Async-");
        executor.initialize();
        return executor;
    }
}

