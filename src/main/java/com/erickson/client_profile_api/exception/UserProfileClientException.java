package com.erickson.client_profile_api.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class UserProfileClientException extends RuntimeException {
    private final ClientErrorType clientErrorType;
    private final List<Object> values;

    public UserProfileClientException(ClientErrorType clientErrorType,
                                      List<Object> values) {
        super();
        this.clientErrorType = clientErrorType;
        this.values = values;
    }
}
