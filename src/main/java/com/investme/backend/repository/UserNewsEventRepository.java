package com.investme.backend.repository;

import com.investme.backend.entity.UserNewsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNewsEventRepository
        extends JpaRepository<UserNewsEvent, Long> {

    Optional<UserNewsEvent>
    findTopByUserIdAndNewsIdAndDurationSecondsIsNullOrderByViewedAtDesc(
            Long userId,
            Long newsId
    );
}
