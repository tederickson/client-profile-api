package com.erickson.client_profile_api.service;

import com.erickson.client_profile_api.domain.UserProfileRequest;
import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import com.erickson.client_profile_api.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        userProfileService = new UserProfileService(userProfileRepository);
    }

    @Test
    void getUserProfile_WithInvalidRequest() {
        UserProfileRequest request = new UserProfileRequest(null, null);

        final var exception = assertThrows(UserProfileClientException.class,
                                           () -> userProfileService.getUserProfile(request));
        assertEquals(ClientErrorType.MISSING_PARAMETER, exception.getClientErrorType());
        assertEquals(2, exception.getValues().size());

        List<String> expectedValues = List.of("id", "addressType");

        for (String value : expectedValues) {
            assertTrue(exception.getValues().contains(value), value);
        }
    }
}