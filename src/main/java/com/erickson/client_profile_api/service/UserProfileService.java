package com.erickson.client_profile_api.service;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AsyncBeneficiaryService asyncBeneficiaryService;

    private static void validate(UserProfileRequest request) {
        List<Object> parameterNames = new ArrayList<>();
        if (request.id() == null) {
            parameterNames.add("id");
        }
        if (request.addressType() == null) {
            parameterNames.add("addressType");
        }

        if (!parameterNames.isEmpty()) {
            throw new UserProfileClientException(ClientErrorType.MISSING_PARAMETER, parameterNames);
        }
    }

    public UserProfileResponse getUserProfile(UserProfileRequest request) {
        validate(request);

        CompletableFuture<List<BeneficiaryDTO>> asyncBeneficiaries =
                asyncBeneficiaryService.getBeneficiaries(request.id());

        UserProfileEntity userProfileEntity = userProfileRepository.findById(request.id())
                .orElseThrow(() -> new UserProfileClientException(ClientErrorType.NOT_FOUND,
                                                                  List.of(request.id())));

        List<BeneficiaryDTO> beneficiaries = asyncBeneficiaries.join();

        return UserProfileMapper.map(userProfileEntity, request.addressType(), beneficiaries);
    }
}
