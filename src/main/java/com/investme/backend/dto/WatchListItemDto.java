package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WatchListItemDto {
    private String stockId;
    private String name;
    private int currentPrice;
    private double changeRate;
    private int changeAmount;
    private LocalDateTime addedAt;
}
