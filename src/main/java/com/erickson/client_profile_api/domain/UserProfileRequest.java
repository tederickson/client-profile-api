package com.erickson.client_profile_api.domain;

import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;

import java.util.ArrayList;
import java.util.List;

public record UserProfileRequest(Long id, AddressType addressType) {
    public void validate() {
        List<Object> parameterNames = new ArrayList<>();
        if (id == null) {
            parameterNames.add("id");
        }
        if (addressType == null) {
            parameterNames.add("addressType");
        }

        if (!parameterNames.isEmpty()) {
            throw new UserProfileClientException(ClientErrorType.MISSING_PARAMETER, parameterNames);
        }
    }
}
