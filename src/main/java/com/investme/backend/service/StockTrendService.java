package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockTrendService {

    private final StockRepository stockRepository;

    @Transactional
    public void updateStockTrends() {

        List<Stock> stocks =
                stockRepository.findAll();

        // 종목 순서를 랜덤하게 섞음
        Collections.shuffle(stocks);

        int total = stocks.size();

        for (int i = 0; i < total; i++) {

            Stock stock = stocks.get(i);

            if (i < 10) {
                stock.updateTrend("UP");

            } else if (i < 20) {
                stock.updateTrend("FLAT");

            } else {
                stock.updateTrend("DOWN");
            }
        }
    }
}
