package com.erickson.client_profile_api.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserProfileResponse {
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private List<Address> addresses;
}
