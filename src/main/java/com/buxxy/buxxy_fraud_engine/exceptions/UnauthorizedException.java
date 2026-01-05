package com.buxxy.buxxy_fraud_engine.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(
                "UNAUTHORIZED",
                message,
                HttpStatus.UNAUTHORIZED
        );
    }
}
