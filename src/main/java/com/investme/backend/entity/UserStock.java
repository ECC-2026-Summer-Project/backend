package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "UserStock")
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holding_id")
    private Long holdingId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stock_id", nullable = false)
    private String stockId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "average_price", nullable = false)
    private int averagePrice;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    public UserStock(
            Long userId,
            String stockId,
            int quantity,
            int averagePrice
    ) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.totalAmount = (long) quantity * averagePrice;
    }

    public void addQuantity(int qty, int price) {

        int totalCost =
                this.averagePrice * this.quantity
                        + price * qty;

        this.quantity += qty;

        this.averagePrice =
                totalCost / this.quantity;

        this.totalAmount =
                (long) this.averagePrice
                        * this.quantity;
    }

    public void reduceQuantity(int qty) {

        this.quantity -= qty;

        this.totalAmount =
                (long) this.averagePrice
                        * this.quantity;
    }
}