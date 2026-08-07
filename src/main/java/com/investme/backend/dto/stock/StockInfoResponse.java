package com.investme.backend.dto.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockInfoResponse {

    private String stockId;
    private String name;
    private String description;
    private String industry;
    private Long marketCap;
    private Double per;
    private Double dividendYield;
    private LocalDate listedDate;
    private String ceo;
    private Integer employees;

    @Builder
    public StockInfoResponse(
            String stockId,
            String name,
            String description,
            String industry,
            Long marketCap,
            Double per,
            Double dividendYield,
            LocalDate listedDate,
            String ceo,
            Integer employees
    ) {
        this.stockId = stockId;
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.marketCap = marketCap;
        this.per = per;
        this.dividendYield = dividendYield;
        this.listedDate = listedDate;
        this.ceo = ceo;
        this.employees = employees;
    }
}