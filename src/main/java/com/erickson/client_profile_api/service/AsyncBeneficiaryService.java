package com.erickson.client_profile_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncBeneficiaryService {
    @Async("asyncTaskExecutor")
    public CompletableFuture<String> getBeneficiaries(long userProfileId) {
        try {
            log.info("-> Starting task: {} on thread: {}", userProfileId, Thread.currentThread().getName());
            // Simulate work
            Thread.sleep(2000 * userProfileId);
            log.info("<- Completed task: {} on thread: {}", userProfileId, Thread.currentThread().getName());

            return CompletableFuture.completedFuture("Done");
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
        }
        return CompletableFuture.completedFuture("Interrupted");
    }
}
