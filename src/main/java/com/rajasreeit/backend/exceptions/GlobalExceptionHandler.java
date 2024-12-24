package com.rajasreeit.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public Map<String, String> handleApiException(ApiException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(ex.getStatusCode()));
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, String> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.put("message", "An unexpected error occurred: " + ex.getMessage());
        return response;
    }
}
