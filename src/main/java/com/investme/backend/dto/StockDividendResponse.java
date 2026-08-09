package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockDividendResponse {

    private int year;
    private int amountPerShare;
    private double yieldRate;
}