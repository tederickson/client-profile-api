package com.erickson.client_profile_api.service;

import com.erickson.client_profile_api.model.BeneficiaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncBeneficiaryService {
    @Async("asyncTaskExecutor")
    public CompletableFuture<List<BeneficiaryDTO>> getBeneficiaries(long userProfileId) {
        try {
            // Simulate work
            Thread.sleep(2000 * userProfileId);

            List<BeneficiaryDTO> results = List.of(
                    new BeneficiaryDTO("firstName",
                                       "lastName",
                                       LocalDate.of(1979, 3, 23),
                                       "ssn",
                                       1.0F,
                                       true)
            );

            return CompletableFuture.completedFuture(results);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
            return CompletableFuture.completedFuture(List.of());
        }
    }
}
