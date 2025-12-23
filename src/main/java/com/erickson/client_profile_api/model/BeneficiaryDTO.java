package com.erickson.client_profile_api.model;

import java.time.LocalDate;

public record BeneficiaryDTO(String firstName,
                             String lastName,
                             LocalDate dateOfBirth,
                             String ssn,
                             Float percentage,
                             Boolean primary) {
}
