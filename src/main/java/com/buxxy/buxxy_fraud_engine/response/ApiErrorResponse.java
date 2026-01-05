package com.buxxy.buxxy_fraud_engine.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ApiErrorResponse {

    private String errorCode;

    private String errorMessage;

    private Map<String,String> errors;

    private LocalDateTime errorOccuredOn;

    public ApiErrorResponse(String errorCode, String errorMessage, Map<String, String> errors, LocalDateTime errorOccuredOn) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errors = errors;
        this.errorOccuredOn = errorOccuredOn;
    }
}
