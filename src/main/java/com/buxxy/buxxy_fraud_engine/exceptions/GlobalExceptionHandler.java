package com.buxxy.buxxy_fraud_engine.exceptions;

import com.buxxy.buxxy_fraud_engine.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String,String> fieldErrors=new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error->fieldErrors.put(error.getField(), error.getDefaultMessage()));


        ApiErrorResponse response=new ApiErrorResponse(
                "VALIDATION FAILED",
                "INPUT VALIDATION FAILED",
                fieldErrors,
                LocalDateTime.now()
        );

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException apiEx){

        ApiErrorResponse response=new ApiErrorResponse(
                apiEx.getErrorCode(),
                apiEx.getMessage(),
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.status(apiEx.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllException(Exception e){

        ApiErrorResponse response=new ApiErrorResponse(
                "INTERNAL SERVER ERROR",
                "Something went wrong. Please try again later",
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
