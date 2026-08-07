package com.investme.backend.dto.stock;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockOrderBookResponse {

    private Long currentPrice;
    private List<OrderBookLevel> asks;
    private List<OrderBookLevel> bids;

    @Builder
    public StockOrderBookResponse(Long currentPrice, List<OrderBookLevel> asks, List<OrderBookLevel> bids) {
        this.currentPrice = currentPrice;
        this.asks = asks;
        this.bids = bids;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderBookLevel {
        private Long price;
        private Long quantity;

        @Builder
        public OrderBookLevel(Long price, Long quantity) {
            this.price = price;
            this.quantity = quantity;
        }
    }
}
