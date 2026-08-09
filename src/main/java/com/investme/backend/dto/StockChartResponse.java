package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockChartResponse {

    private LocalDateTime time;
    private long open;
    private long high;
    private long low;
    private long close;
    private long volume;
}