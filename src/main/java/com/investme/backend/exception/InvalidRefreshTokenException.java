package com.investme.backend.exception;

public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException() {
        super("Refresh Token이 만료되었거나 유효하지 않습니다.");
    }

}
