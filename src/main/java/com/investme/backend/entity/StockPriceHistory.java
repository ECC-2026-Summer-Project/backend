package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "stock_price_history")
public class StockPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "stock_id", nullable = false)
    private String stockId;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public StockPriceHistory(
            String stockId,
            Integer price
    ) {
        this.stockId = stockId;
        this.price = price;
        this.createdAt = LocalDateTime.now();
    }
}