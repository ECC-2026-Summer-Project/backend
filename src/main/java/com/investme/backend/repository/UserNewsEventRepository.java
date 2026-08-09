package com.investme.backend.repository;

import com.investme.backend.entity.UserNewsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNewsEventRepository
        extends JpaRepository<UserNewsEvent, Long> {

    // 뉴스 열람 기록 API에서 사용
    Optional<UserNewsEvent>
    findTopByUserIdAndNewsIdAndDurationSecondsIsNullOrderByViewedAtDesc(
            Long userId,
            Long newsId
    );

    // 레포트 생성 시 사용자의 전체 뉴스 열람 기록 조회
    List<UserNewsEvent> findAllByUserId(Long userId);
}
