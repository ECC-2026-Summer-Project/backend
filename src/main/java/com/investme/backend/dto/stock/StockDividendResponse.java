package com.investme.backend.dto.stock;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockDividendResponse {

    private Integer year;
    private Integer amountPerShare;
    private Double yieldRate;

    @Builder
    public StockDividendResponse(Integer year, Integer amountPerShare, Double yieldRate) {
        this.year = year;
        this.amountPerShare = amountPerShare;
        this.yieldRate = yieldRate;
    }
}
