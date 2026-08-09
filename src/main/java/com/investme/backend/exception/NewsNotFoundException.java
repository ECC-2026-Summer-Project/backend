package com.investme.backend.exception;

public class NewsNotFoundException extends RuntimeException {

    public NewsNotFoundException() {
        super("존재하지 않는 뉴스입니다.");
    }
}
