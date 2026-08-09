package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HoldingResponse {

    private String stockId;
    private String stockName;
    private Integer quantity;
    private Integer averagePurchasePrice;
    private Integer currentPrice;
    private Integer priceChange;
    private Double changeRate;
    private Long purchaseAmount;
    private Long evaluationAmount;
    private Long profitLoss;
    private Double returnRate;
}