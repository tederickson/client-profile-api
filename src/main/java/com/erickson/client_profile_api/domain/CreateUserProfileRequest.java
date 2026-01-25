package com.erickson.client_profile_api.domain;

import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record CreateUserProfileRequest(String firstName,
                                       String lastName,
                                       LocalDate dateOfBirth,
                                       List<Address> addresses) {
    public void validate() {
        List<Object> parameterNames = new ArrayList<>();

        if (StringUtils.isBlank(firstName)) {
            parameterNames.add("firstName");
        }
        if (StringUtils.isBlank(lastName)) {
            parameterNames.add("lastName");
        }
        if (dateOfBirth == null) {
            parameterNames.add("dateOfBirth");
        }
        if (CollectionUtils.isEmpty(addresses)) {
            parameterNames.add("addresses");
        }

        if (!parameterNames.isEmpty()) {
            throw new UserProfileClientException(ClientErrorType.MISSING_PARAMETER, parameterNames);
        }
    }
}
