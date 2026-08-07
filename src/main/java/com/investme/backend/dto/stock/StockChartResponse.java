package com.investme.backend.dto.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockChartResponse {

    private LocalDateTime time;
    private Long open;
    private Long high;
    private Long low;
    private Long close;
    private Long volume;

    @Builder
    public StockChartResponse(LocalDateTime time, Long open, Long high,
                              Long low, Long close, Long volume) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}