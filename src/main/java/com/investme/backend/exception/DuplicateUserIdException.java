package com.investme.backend.exception;

public class DuplicateUserIdException extends RuntimeException {

    public DuplicateUserIdException() {
        super("이미 존재하는 아이디입니다.");
    }

}