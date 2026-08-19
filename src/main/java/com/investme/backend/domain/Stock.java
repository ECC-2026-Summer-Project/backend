package com.investme.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Stock {

    @Id
    @Column(length = 20)
    private String stockId; // ex) "005930"

    private String name;

    private String market;

    private String sector;

    private int currentPrice;

    private double changeRate;

    private int changeAmount;

    private long volume;

    private long marketCap;

    // 가상 주가 알고리즘에서 사용
    private String trend;

    private Double volatility;

    private LocalDateTime lastUpdated;


    public void updatePrice(int newPrice) {

        this.changeAmount =
                newPrice - this.currentPrice;

        this.changeRate =
                this.currentPrice == 0
                        ? 0.0
                        : (double) this.changeAmount
                        / this.currentPrice
                        * 100;

        this.currentPrice =
                newPrice;

        this.lastUpdated =
                LocalDateTime.now();
    }
}
