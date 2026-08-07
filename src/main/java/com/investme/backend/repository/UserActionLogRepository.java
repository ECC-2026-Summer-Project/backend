package com.investme.backend.repository;

import com.investme.backend.entity.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActionLogRepository
        extends JpaRepository<UserActionLog, Long> {

    List<UserActionLog>
    findAllByUserIdAndTargetType(
            Long userId,
            String targetType
    );
}
