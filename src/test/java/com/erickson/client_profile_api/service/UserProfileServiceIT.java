package com.erickson.client_profile_api.service;

import com.erickson.client_profile_api.domain.Address;
import com.erickson.client_profile_api.domain.AddressType;
import com.erickson.client_profile_api.domain.UserProfileRequest;
import com.erickson.client_profile_api.domain.UserProfileResponse;
import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class UserProfileServiceIT {

    @Autowired
    private UserProfileService userProfileService;

    @Test
    void getUserProfile_withInvalidId() {
        UserProfileRequest request = new UserProfileRequest(-1L, AddressType.HOME);

        final var exception = assertThrows(UserProfileClientException.class,
                                           () -> userProfileService.getUserProfile(request));
        assertEquals(ClientErrorType.NOT_FOUND, exception.getClientErrorType());
        assertEquals(1, exception.getValues().size());

        assertEquals(request.id(), exception.getValues().getFirst());
    }

    @Test
    void getUserProfile_withWorkAddress() {
        UserProfileRequest request = new UserProfileRequest(1L, AddressType.WORK);

        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(request);
        assertFalse(userProfileResponse.getAddresses().isEmpty());

        Address address = userProfileResponse.getAddresses().getFirst();
        assertEquals("Blue Cube", address.line1());

        assertEquals(1, userProfileResponse.getBeneficiaries().size());
    }

    @Test
    void getUserProfile_withNoAddress() {
        UserProfileRequest request = new UserProfileRequest(2L, AddressType.ALL);

        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(request);
        assertTrue(userProfileResponse.getAddresses().isEmpty());

        assertEquals(1, userProfileResponse.getBeneficiaries().size());
    }

    @Test
    void getUserProfile() {
        UserProfileRequest request = new UserProfileRequest(1L, AddressType.ALL);

        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(request);
        assertEquals(2, userProfileResponse.getAddresses().size());

        assertEquals(1, userProfileResponse.getBeneficiaries().size());
    }
}