package com.investme.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long watchlistId;

    private Long userId;
    private String stockId;
    private LocalDateTime createdAt;

    public WatchList(Long userId, String stockId) {
        this.userId = userId;
        this.stockId = stockId;
        this.createdAt = LocalDateTime.now();
    }
}
