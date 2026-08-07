package com.investme.backend.exception;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String stockId) {
        super("존재하지 않는 종목입니다. stockId=" + stockId);
    }
}