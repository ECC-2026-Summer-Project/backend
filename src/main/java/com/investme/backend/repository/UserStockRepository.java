package com.investme.backend.repository;

import com.investme.backend.domain.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    Optional<UserStock> findByUserIdAndStockId(Long userId, String stockId);
}
