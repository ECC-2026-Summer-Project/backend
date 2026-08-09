package com.investme.backend.repository;

import com.investme.backend.domain.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    List<WatchList> findByUserId(Long userId);
    Optional<WatchList> findByUserIdAndStockId(Long userId, String stockId);
    void deleteByUserIdAndStockId(Long userId, String stockId);
}
