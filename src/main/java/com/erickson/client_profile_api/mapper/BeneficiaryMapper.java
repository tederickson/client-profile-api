package com.erickson.client_profile_api.mapper;

import com.erickson.client_profile_api.domain.Beneficiary;
import com.erickson.client_profile_api.model.BeneficiaryDTO;

public class BeneficiaryMapper {
    private BeneficiaryMapper() {
    }

    public static Beneficiary map(BeneficiaryDTO dto) {
        return new Beneficiary(dto.firstName(), dto.lastName(), dto.dateOfBirth());
    }
}
