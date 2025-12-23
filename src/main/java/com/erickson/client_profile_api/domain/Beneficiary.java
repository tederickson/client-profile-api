package com.erickson.client_profile_api.domain;

import java.time.LocalDate;

public record Beneficiary(String firstName, String lastName, LocalDate dateOfBirth) {
}
