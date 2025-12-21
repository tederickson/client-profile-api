package com.erickson.client_profile_api.domain;

import lombok.Builder;

@Builder
public record Address(String line1,
                      String line2,
                      String city,
                      String state,
                      String zipCode,
                      AddressType addressType) {
}
