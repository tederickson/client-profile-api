package com.erickson.client_profile_api.service;

import com.erickson.client_profile_api.domain.AddressType;
import com.erickson.client_profile_api.domain.UserProfileRequest;
import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import com.erickson.client_profile_api.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class UserProfileServiceIT {
    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        userProfileService = new UserProfileService(userProfileRepository);
    }

    @Test
    void getUserProfile_withInvalidId() {
        UserProfileRequest request = new UserProfileRequest(-1L, AddressType.HOME);

        final var exception = assertThrows(UserProfileClientException.class,
                                           () -> userProfileService.getUserProfile(request));
        assertEquals(ClientErrorType.NOT_FOUND, exception.getClientErrorType());
        assertEquals(1, exception.getValues().size());

        assertEquals(request.id(), exception.getValues().getFirst());
    }
}