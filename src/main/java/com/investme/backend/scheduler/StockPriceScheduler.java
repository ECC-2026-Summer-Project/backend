package com.investme.backend.scheduler;

import com.investme.backend.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockPriceScheduler {

    private final StockPriceService stockPriceService;

    @Scheduled(fixedRate = 10000)
    public void updateStockPrices() {
        stockPriceService.updateStockPrices();
    }
}