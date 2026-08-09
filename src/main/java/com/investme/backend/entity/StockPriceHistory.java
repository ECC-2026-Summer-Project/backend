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

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public StockPriceHistory(
            String companyId,
            Integer price
    ) {
        this.companyId = companyId;
        this.price = price;
        this.createdAt = LocalDateTime.now();
    }
}