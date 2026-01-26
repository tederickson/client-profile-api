package com.erickson.client_profile_api.domain;

import com.erickson.client_profile_api.exception.ClientErrorType;
import com.erickson.client_profile_api.exception.UserProfileClientException;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
public record Address(Long id,
                      String line1,
                      String line2,
                      String city,
                      String state,
                      String zipCode,
                      AddressType addressType) {
    public void validate() {
        List<Object> parameterNames = new ArrayList<>();

        if (StringUtils.isBlank(line1)) {
            parameterNames.add("line1");
        }
        if (StringUtils.isBlank(city)) {
            parameterNames.add("city");
        }
        if (StringUtils.isBlank(state)) {
            parameterNames.add("state");
        }
        if (StringUtils.isBlank(zipCode)) {
            parameterNames.add("zipCode");
        }
        if (addressType == null) {
            parameterNames.add("addressType");
        }

        if (!parameterNames.isEmpty()) {
            throw new UserProfileClientException(ClientErrorType.MISSING_PARAMETER, parameterNames);
        }
    }
}
