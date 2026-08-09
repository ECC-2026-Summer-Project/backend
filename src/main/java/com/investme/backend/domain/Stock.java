package com.investme.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

public class Stock {
    @Id
    @Column(length = 20)
    private String stockId; // ex) "005930"

    private String name;
    private String market;       // ex) KOSPI / KOSDAQ
    private String sector;
    private int currentPrice;
    private double changeRate;
    private int changeAmount;
    private long volume;
    private long marketCap;
}
