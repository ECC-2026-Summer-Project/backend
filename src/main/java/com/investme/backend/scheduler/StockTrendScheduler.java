package com.investme.backend.scheduler;

import com.investme.backend.service.StockTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockTrendScheduler {

    private final StockTrendService stockTrendService;

    // 10분마다 종목 추세 재배정
    @Scheduled(fixedRate = 420000)
    public void updateStockTrends() {
        stockTrendService.updateStockTrends();
    }
}
