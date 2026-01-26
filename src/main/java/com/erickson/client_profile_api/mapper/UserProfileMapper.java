package com.erickson.client_profile_api.mapper;

import com.erickson.client_profile_api.domain.Address;
import com.erickson.client_profile_api.domain.AddressType;
import com.erickson.client_profile_api.domain.CreateUserProfileRequest;
import com.erickson.client_profile_api.domain.UserProfileResponse;
import com.erickson.client_profile_api.model.AddressEntity;
import com.erickson.client_profile_api.model.BeneficiaryDTO;
import com.erickson.client_profile_api.model.UserProfileEntity;

import java.util.List;

public class UserProfileMapper {
    private UserProfileMapper() {
    }

    public static UserProfileResponse map(final UserProfileEntity userProfileEntity,
                                          final AddressType addressType,
                                          final List<BeneficiaryDTO> beneficiaries) {
        UserProfileResponse userProfileResponse = buildUserProfileResponse(userProfileEntity);
        final List<Address> addresses;

        if (AddressType.ALL.equals(addressType)) {
            addresses = userProfileEntity.getAddressEntities().stream()
                    .map(AddressMapper::map)
                    .toList();
        }
        else {
            addresses = userProfileEntity.getAddressEntities().stream()
                    .filter(addressEntity -> addressType.equals(addressEntity.getAddressType()))
                    .map(AddressMapper::map)
                    .toList();
        }

        userProfileResponse.setAddresses(addresses);

        userProfileResponse.setBeneficiaries(beneficiaries.stream().map(BeneficiaryMapper::map).toList());

        return userProfileResponse;
    }

    public static UserProfileResponse map(UserProfileEntity userProfileEntity) {
        UserProfileResponse userProfileResponse = buildUserProfileResponse(userProfileEntity);

        final List<Address> addresses = userProfileEntity.getAddressEntities().stream()
                .map(AddressMapper::map)
                .toList();

        userProfileResponse.setAddresses(addresses);

        return userProfileResponse;
    }

    public static UserProfileEntity map(final CreateUserProfileRequest createUserProfileRequest) {
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        List<AddressEntity> addressEntities = createUserProfileRequest.addresses().stream()
                .map(address -> AddressMapper.create(address, userProfileEntity))
                .toList();
        userProfileEntity.setAddressEntities(addressEntities);

        userProfileEntity.setFirstName(createUserProfileRequest.firstName());
        userProfileEntity.setLastName(createUserProfileRequest.lastName());
        userProfileEntity.setDateOfBirth(createUserProfileRequest.dateOfBirth());

        return userProfileEntity;
    }

    private static UserProfileResponse buildUserProfileResponse(UserProfileEntity userProfileEntity) {
        UserProfileResponse userProfileResponse = new UserProfileResponse();

        userProfileResponse.setId(userProfileEntity.getUserProfileId());
        userProfileResponse.setFirstName(userProfileEntity.getFirstName());
        userProfileResponse.setLastName(userProfileEntity.getLastName());
        userProfileResponse.setDateOfBirth(userProfileEntity.getDateOfBirth());

        return userProfileResponse;
    }
}
