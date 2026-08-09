package com.investme.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holdingId;

    private Long userId;
    private String stockId;
    private int quantity;
    private int averagePrice;

    public UserStock(Long userId, String stockId, int quantity, int averagePrice) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public void addQuantity(int qty, int price) {
        int totalCost = this.averagePrice * this.quantity + price * qty;
        this.quantity += qty;
        this.averagePrice = totalCost / this.quantity;
    }

    public void reduceQuantity(int qty) {
        this.quantity -= qty;
    }
}
