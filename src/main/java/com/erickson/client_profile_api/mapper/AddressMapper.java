package com.erickson.client_profile_api.mapper;

import com.erickson.client_profile_api.domain.Address;
import com.erickson.client_profile_api.model.AddressEntity;
import com.erickson.client_profile_api.model.UserProfileEntity;

public class AddressMapper {
    private AddressMapper() {
    }

    public static Address map(final AddressEntity addressEntity) {
        return Address.builder()
                .id(addressEntity.getAddressId())
                .addressType(addressEntity.getAddressType())
                .line1(addressEntity.getLine1())
                .line2(addressEntity.getLine2())
                .city(addressEntity.getCity())
                .state(addressEntity.getState())
                .zipCode(addressEntity.getZipCode())
                .build();
    }

    public static AddressEntity create(final Address address, final UserProfileEntity userProfileEntity) {
        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setUserProfileEntity(userProfileEntity);

        addressEntity.setAddressType(address.addressType());
        addressEntity.setLine1(address.line1());
        addressEntity.setLine2(address.line2());
        addressEntity.setCity(address.city());
        addressEntity.setState(address.state());
        addressEntity.setZipCode(address.zipCode());

        return addressEntity;
    }
}
