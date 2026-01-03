package com.buxxy.buxxy_fraud_engine.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(message, "VALIDATION_FAILED", HttpStatus.BAD_REQUEST);
    }
}
