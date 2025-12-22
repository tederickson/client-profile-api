package com.erickson.client_profile_api.rest;

import com.erickson.client_profile_api.domain.AddressType;
import com.erickson.client_profile_api.domain.UserProfileRequest;
import com.erickson.client_profile_api.domain.UserProfileResponse;
import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import com.erickson.client_profile_api.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/user_profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/id/{id}/addressType/{addressType}")
    public UserProfileResponse getUserProfile(@PathVariable long id,
                                              @PathVariable String addressType) {
        AddressType addressType1;
        try {
            addressType1 = AddressType.valueOf(addressType);
        } catch (IllegalArgumentException e) {
            throw new UserProfileClientException(ClientErrorType.INVALID_ADDRESS_TYPE, List.of(addressType));
        }
        UserProfileRequest request = new UserProfileRequest(id, addressType1);

        return userProfileService.getUserProfile(request);
    }
}
