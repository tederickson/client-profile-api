package com.erickson.client_profile_api.rest;

import com.erickson.client_profile_api.exception.UserProfileClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(UserProfileClientException.class)
    public ResponseEntity<String> handleUserProfileClientException(UserProfileClientException exception) {
        return switch (exception.getClientErrorType()) {
            case MISSING_PARAMETER -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing parameters " + exception.getValues());
            case INVALID_ADDRESS_TYPE -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid address type " + exception.getValues());
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Did not find UserProfile id " + exception.getValues().getFirst());
        };
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUserProfileClientException(Exception exception) {
        log.error("Please write a ticket for this scenario", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
