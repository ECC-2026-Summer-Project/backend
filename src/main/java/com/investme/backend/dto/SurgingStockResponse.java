package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurgingStockResponse {

    private Long eventId;
    
    private String stockId;
    private String stockName;
    private Integer currentPrice;
    private Integer priceChange;
    private Double changeRate;
}
