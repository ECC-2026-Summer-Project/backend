package com.investme.backend.exception;

import com.investme.backend.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        ErrorResponse body = new ErrorResponse(false, null, new ErrorResponse.ErrorDetail(e.getCode(), e.getMessage()));
        return ResponseEntity.status(e.getStatus()).body(body);
    }
}
