package com.investme.backend.repository;

import com.investme.backend.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, String> {
    @Query("SELECT s FROM Stock s WHERE " +
            "(:keyword IS NULL OR s.name LIKE %:keyword%) AND " +
            "(:sector IS NULL OR s.sector = :sector)")
    Page<Stock> search(@Param("keyword") String keyword,
                       @Param("sector") String sector,
                       Pageable pageable);
}
