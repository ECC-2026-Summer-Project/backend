package com.investme.backend.repository;

import com.investme.backend.entity.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {

    List<UserStock> findAllByUserId(Long userId);
}