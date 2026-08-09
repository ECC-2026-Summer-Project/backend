package com.investme.backend.exception;

public class NewsViewNotStartedException extends RuntimeException {

    public NewsViewNotStartedException() {
        super("뉴스 열람 시작 기록이 없습니다.");
    }
}
