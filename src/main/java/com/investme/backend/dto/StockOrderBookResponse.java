package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StockOrderBookResponse {

    private long currentPrice;
    private List<OrderBookLevel> asks;
    private List<OrderBookLevel> bids;
}