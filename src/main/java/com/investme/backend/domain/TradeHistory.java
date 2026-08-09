package com.investme.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    private Long userId;
    private String stockId;
    private String side;
    private String orderType;
    private int quantity;
    private int price;
    private long totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private Long triggeredByEventId;

    public TradeHistory(Long userId, String stockId, String side, String orderType,
                         int quantity, int price, long totalAmount, String status, Long triggeredByEventId) {
        this.userId = userId;
        this.stockId = stockId;
        this.side = side;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.triggeredByEventId = triggeredByEventId;
    }
}
