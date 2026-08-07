package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PortfolioResponse {

    private Long totalPurchaseAmount;
    private Long totalEvaluationAmount;
    private Long totalProfitLoss;
    private Double totalReturnRate;
    private List<HoldingResponse> holdings;
}