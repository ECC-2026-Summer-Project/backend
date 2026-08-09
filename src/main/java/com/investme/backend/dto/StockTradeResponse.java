package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockTradeResponse {

    private LocalDateTime time;
    private int price;
    private int quantity;
    private String side;
}