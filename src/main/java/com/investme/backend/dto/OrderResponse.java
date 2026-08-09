package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private boolean success;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private String orderId;
        private String stockId;
        private String stockName;
        private String side;
        private String orderType;
        private int quantity;
        private int price;
        private long totalAmount;
        private String status;
        private LocalDateTime createdAt;
    }
}
