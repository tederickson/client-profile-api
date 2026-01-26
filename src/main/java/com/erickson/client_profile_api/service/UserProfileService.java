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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public UserProfileResponse createUserProfile(CreateUserProfileRequest createUserProfileRequest) {
        createUserProfileRequest.validate();
        createUserProfileRequest.addresses().forEach(Address::validate);

        UserProfileEntity userProfileEntity = UserProfileMapper.map(createUserProfileRequest);

        UserProfileEntity dbUserProfileEntity = userProfileRepository.save(userProfileEntity);

        return UserProfileMapper.map(dbUserProfileEntity);
    }
}
