package com.investme.backend.dto.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockSummaryResponse {

    private String stockId;
    private String name;
    private String market;
    private Long currentPrice;
    private Double changeRate;
    private Long changeAmount;
    private Long openPrice;
    private Long highPrice;
    private Long lowPrice;
    private Long volume;
    private Boolean isAiRecommended;

    @Builder
    public StockSummaryResponse(String stockId, String name, String market,
                                Long currentPrice, Double changeRate, Long changeAmount,
                                Long openPrice, Long highPrice, Long lowPrice,
                                Long volume, Boolean isAiRecommended) {
        this.stockId = stockId;
        this.name = name;
        this.market = market;
        this.currentPrice = currentPrice;
        this.changeRate = changeRate;
        this.changeAmount = changeAmount;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
        this.isAiRecommended = isAiRecommended;
    }
}
