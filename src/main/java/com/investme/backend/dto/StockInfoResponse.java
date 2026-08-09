package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockInfoResponse {

    private String stockId;
    private String name;
    private String description;
    private String industry;
    private long marketCap;
    private double per;
    private double dividendYield;
    private String listedDate;
    private String ceo;
    private int employees;
}