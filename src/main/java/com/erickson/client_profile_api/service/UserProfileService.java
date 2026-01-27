package com.erickson.client_profile_api.service;

import com.erickson.client_profile_api.domain.Address;
import com.erickson.client_profile_api.domain.CreateUserProfileRequest;
import com.erickson.client_profile_api.domain.UserProfileRequest;
import com.erickson.client_profile_api.domain.UserProfileResponse;
import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import com.erickson.client_profile_api.mapper.UserProfileMapper;
import com.erickson.client_profile_api.model.BeneficiaryDTO;
import com.erickson.client_profile_api.model.UserProfileEntity;
import com.erickson.client_profile_api.repository.UserProfileRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AsyncBeneficiaryService asyncBeneficiaryService;

    public UserProfileResponse getUserProfile(UserProfileRequest request) {
        request.validate();

        CompletableFuture<List<BeneficiaryDTO>> asyncBeneficiaries =
                asyncBeneficiaryService.getBeneficiaries(request.id());
        CompletableFuture<UserProfileEntity> asyncUserProfileEntity = getUserProfileEntity(request.id());
        CompletableFuture.allOf(asyncUserProfileEntity, asyncBeneficiaries).join();

        try {
            List<BeneficiaryDTO> beneficiaries = asyncBeneficiaries.get();

            return UserProfileMapper.map(asyncUserProfileEntity.get(), request.addressType(), beneficiaries);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Interrupted", e);
            throw new RuntimeException(e);
        }
    }

    @Async("asyncTaskExecutor")
    private CompletableFuture<UserProfileEntity> getUserProfileEntity(long userProfileId) {
        UserProfileEntity userProfileEntity = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new UserProfileClientException(ClientErrorType.NOT_FOUND, List.of(userProfileId)));

        return CompletableFuture.completedFuture(userProfileEntity);
    }

    @Retryable(noRetryFor = UserProfileClientException.class, maxAttempts = 2, backoff = @Backoff(delay = 200))
    public UserProfileResponse createUserProfile(CreateUserProfileRequest createUserProfileRequest) {
        log.info("Creating user profile {}", createUserProfileRequest);
        createUserProfileRequest.validate();
        createUserProfileRequest.addresses().forEach(Address::validate);

        UserProfileEntity userProfileEntity = UserProfileMapper.map(createUserProfileRequest);

        // Replace the save with this line to verify the method is retried
        //  throw new OptimisticLockException("wahoo!");
        UserProfileEntity dbUserProfileEntity = userProfileRepository.save(userProfileEntity);

        return UserProfileMapper.map(dbUserProfileEntity);
    }

    // This method is called if createUserProfile() fails after multiple attempts
    @Recover
    public UserProfileResponse recover(OptimisticLockException e) {
        log.error("Recovery logic executed.", e);
        return new UserProfileResponse();
    }

    // This method is called as soon as the UserProfileClientException is thrown
    @Recover
    public UserProfileResponse handleUserProfileClientException(UserProfileClientException e) {
        log.info("UserProfileClientException Recovery logic executed.");
        throw e;
    }
}
