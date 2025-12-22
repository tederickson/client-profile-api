package com.erickson.client_profile_api.mapper;

import com.erickson.client_profile_api.domain.Address;
import com.erickson.client_profile_api.model.AddressEntity;

public class AddressMapper {
    private AddressMapper() {
    }

    public static Address map(final AddressEntity addressEntity) {
        return Address.builder()
                .addressType(addressEntity.getAddressType())
                .line1(addressEntity.getLine1())
                .line2(addressEntity.getLine2())
                .city(addressEntity.getCity())
                .state(addressEntity.getState())
                .zipCode(addressEntity.getZipCode())
                .build();
    }
}
