package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_news_event")
public class UserNewsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "news_id", nullable = false)
    private Long newsId;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    public UserNewsEvent(
            Long userId,
            Long newsId
    ) {
        this.userId = userId;
        this.newsId = newsId;
        this.viewedAt = LocalDateTime.now();
    }

    public void completeView(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
