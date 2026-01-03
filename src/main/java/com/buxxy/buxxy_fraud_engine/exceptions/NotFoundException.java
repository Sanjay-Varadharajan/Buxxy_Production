    package com.buxxy.buxxy_fraud_engine.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
        public NotFoundException(String message) {
            super(message, "NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }
