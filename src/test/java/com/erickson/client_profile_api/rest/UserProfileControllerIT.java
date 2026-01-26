package com.erickson.client_profile_api.rest;

import com.erickson.client_profile_api.ClientProfileApiApplication;
import com.erickson.client_profile_api.domain.Address;
import com.erickson.client_profile_api.domain.AddressType;
import com.erickson.client_profile_api.domain.CreateUserProfileRequest;
import com.erickson.client_profile_api.domain.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void getUserProfile_InvalidId() {
        String url = createURLWithPort("/v1/user_profile/id/apple/addressType/ALL");

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        String message = response.getBody();
        assertNotNull(message);
        System.out.println("message = " + message);
        assertTrue(message.startsWith("Method parameter 'id': Failed to convert value of type"));
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

    @Test
    void createUserProfile_MissingFields() {
        String url = createURLWithPort("/v1/user_profile/");
        CreateUserProfileRequest request = new CreateUserProfileRequest("  ", "", null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.hasBody());

        String message = response.getBody();

        assertNotNull(message);
        assertTrue(message.startsWith("Missing parameters "));
    }

    @Test
    void createUserProfile_MissingAddressFields() {
        String url = createURLWithPort("/v1/user_profile/");
        CreateUserProfileRequest request = new CreateUserProfileRequest("George", "Orwell", LocalDate.now(),
                                                                        List.of(new Address(null, " ", null, "", "", "",
                                                                                            null)));

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.hasBody());

        String message = response.getBody();
        System.out.println("message = " + message);
        assertNotNull(message);
        assertTrue(message.startsWith("Missing parameters "));
    }

    @Test
    void createUserProfile() {
        String url = createURLWithPort("/v1/user_profile/");
        LocalDate dateOfBirth = LocalDate.now();
        CreateUserProfileRequest request = new CreateUserProfileRequest("George", "Orwell", dateOfBirth,
                                                                        List.of(new Address(null,
                                                                                            "123 Main St",
                                                                                            "Apt 3A",
                                                                                            "Somewhere",
                                                                                            "CO",
                                                                                            "80123",
                                                                                            AddressType.WORK)));

        ResponseEntity<UserProfileResponse> response = restTemplate.postForEntity(url, request,
                                                                                  UserProfileResponse.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());

        UserProfileResponse body = response.getBody();
        assertNotNull(body);

        assertEquals("George", body.getFirstName());
        assertEquals("Orwell", body.getLastName());
        assertEquals(dateOfBirth, body.getDateOfBirth());

        assertEquals(1, body.getAddresses().size());
        Address address = body.getAddresses().getFirst();

        assertEquals("123 Main St", address.line1());
        assertEquals("Apt 3A", address.line2());
        assertEquals("Somewhere", address.city());
        assertEquals("CO", address.state());
        assertEquals("80123", address.zipCode());
        assertEquals(AddressType.WORK, address.addressType());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}