package com.investme.backend.repository;

import com.investme.backend.entity.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserActionLogRepository
        extends JpaRepository<UserActionLog, Long> {

    List<UserActionLog> findAllByUserIdAndTargetType(
            Long userId,
            String targetType
    );

    Optional<UserActionLog> findByActionIdAndUserId(
            Long actionId,
            Long userId
    );
}
