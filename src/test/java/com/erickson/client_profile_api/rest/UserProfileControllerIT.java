package com.erickson.client_profile_api.rest;

import com.erickson.client_profile_api.ClientProfileApiApplication;
import com.erickson.client_profile_api.domain.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ClientProfileApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data/InitializeTests.sql")
class UserProfileControllerIT {
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;

    @Test
    void getUserProfile() {
        String url = createURLWithPort("/v1/user_profile/id/1/addressType/ALL");

        UserProfileResponse response = restTemplate.getForEntity(url, UserProfileResponse.class).getBody();
        assertNotNull(response);
        assertEquals("Richard", response.getFirstName());
        assertEquals(2, response.getAddresses().size());
    }

    @Test
    void getUserProfile_NotFound() {
        String url = createURLWithPort("/v1/user_profile/id/22/addressType/ALL");

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Did not find UserProfile id 22", response.getBody());
    }

    @Test
    void getUserProfile_MissingAddressType() {
        String url = createURLWithPort("/v1/user_profile/id/4/addressType/");

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.hasBody());

        String message = response.getBody();
        assertNotNull(message);
        assertTrue(message.startsWith("No static resource v1/user_profile/id/4/addressType"));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}