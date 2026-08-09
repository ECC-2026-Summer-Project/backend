package com.investme.backend.exception;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(DuplicateUserIdException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateUserId(DuplicateUserIdException e) {
        ApiResponse<Void> response = new ApiResponse<>(false, null, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidLogin(InvalidLoginException e) {
        ApiResponse<Void> response = new ApiResponse<>(false, null, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRefreshToken(InvalidRefreshTokenException e) {
        ApiResponse<Void> response = new ApiResponse<>(false, null, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNewsNotFound(NewsNotFoundException e) {
        ApiResponse<Void> response = new ApiResponse<>(false, null, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NewsViewNotStartedException.class)
    public ResponseEntity<ApiResponse<Void>> handleNewsViewNotStarted(NewsViewNotStartedException e) {
        ApiResponse<Void> response = new ApiResponse<>(false, null, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

