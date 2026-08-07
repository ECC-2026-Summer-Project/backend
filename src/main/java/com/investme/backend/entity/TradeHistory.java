package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long tradeId;

    @Column(name = "user_id", nullable = false)
    private Long userId; // ⚠️ 회원 Entity의 PK 타입이 Long이 아니라면(예: String) 이 타입도 맞춰서 변경하세요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false, length = 10)
    private TradeType tradeType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "traded_at", nullable = false)
    private LocalDateTime tradedAt;

    @Builder
    public TradeHistory(Long userId, Company company, TradeType tradeType,
                        Integer quantity, Long price, LocalDateTime tradedAt) {
        this.userId = userId;
        this.company = company;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.tradedAt = tradedAt;
    }
}
