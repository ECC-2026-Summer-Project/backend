package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountSummaryResponse {

    private Long totalAssetAmount;
    private Long totalProfitLoss;
    private Double totalReturnRate;
}