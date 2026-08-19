package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_action_log")
public class UserActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Long actionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stock_id")
    private String stockId;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UserActionLog(
            Long userId,
            String stockId,
            String actionType,
            String targetType,
            Long targetId
    ) {
        this.userId = userId;
        this.stockId = stockId;
        this.actionType = actionType;
        this.targetType = targetType;
        this.targetId = targetId;
        this.createdAt = LocalDateTime.now();
    }
}