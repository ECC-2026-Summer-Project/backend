package com.investme.backend.dto.stock;

import com.investme.backend.entity.TradeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockTradeResponse {

    private LocalDateTime time;
    private Long price;
    private Integer quantity;
    private TradeType side;

    @Builder
    public StockTradeResponse(LocalDateTime time, Long price, Integer quantity, TradeType side) {
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }
}