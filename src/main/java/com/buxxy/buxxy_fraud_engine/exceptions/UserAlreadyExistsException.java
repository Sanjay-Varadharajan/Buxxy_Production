package com.buxxy.buxxy_fraud_engine.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {

    public UserAlreadyExistsException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }

    public UserAlreadyExistsException(String message) {
        super(message, "User Already Exists", HttpStatus.CONFLICT);
    }


}
