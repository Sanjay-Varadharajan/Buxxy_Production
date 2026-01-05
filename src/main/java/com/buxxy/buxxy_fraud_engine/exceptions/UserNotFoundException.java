package com.buxxy.buxxy_fraud_engine.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException{

    public UserNotFoundException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }

    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
