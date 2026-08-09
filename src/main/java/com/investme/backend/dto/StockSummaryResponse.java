package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockSummaryResponse {

    private String stockId;
    private String name;
    private String market;
    private int currentPrice;
    private double changeRate;
    private int changeAmount;
    private int openPrice;
    private int highPrice;
    private int lowPrice;
    private long volume;
    private boolean isAiRecommended;
}