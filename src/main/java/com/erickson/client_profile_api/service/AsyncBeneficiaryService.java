package com.erickson.client_profile_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncBeneficiaryService {
    @Async("asyncTaskExecutor")
    public CompletableFuture<List<String>> getBeneficiaries(long userProfileId) {
        try {
            log.info("-> Starting task: {} on thread: {}", userProfileId, Thread.currentThread().getName());
            // Simulate work
            Thread.sleep(2000 * userProfileId);
            log.info("<- Completed task: {} on thread: {}", userProfileId, Thread.currentThread().getName());

            List<String> results = new ArrayList<>();
            for (int i = 0; i < userProfileId; i++) {
                results.add("Done");
            }

            return CompletableFuture.completedFuture(results);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
        }
        return CompletableFuture.completedFuture(List.of("Interrupted"));
    }
}
