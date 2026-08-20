package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.entity.StockPriceHistory;
import com.investme.backend.repository.StockPriceHistoryRepository;
import com.investme.backend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockPriceService {

    private final StockRepository stockRepository;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;

    @Transactional
    public void updateStockPrices() {

        // 1. 모든 종목 조회
        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock : stocks) {

            int currentPrice = stock.getCurrentPrice();

            // 2. 종목별 변동성 가져오기
            double volatility = stock.getVolatility() == null
                    ? 0.0001
                    : stock.getVolatility();

            // 3. -volatility ~ +volatility 범위 랜덤 변동
            double randomChange =
                    (Math.random() * 2 - 1) * volatility;

            // 4. 종목 추세 반영
            double trendEffect =
                    getTrendEffect(stock.getTrend());

            double changeRate =
                    randomChange + trendEffect;

            // 5. 새로운 가격 계산
            int newPrice = (int) Math.round(
                    currentPrice * (1 + changeRate)
            );

            newPrice = Math.max(newPrice, 1);

            // 6. 가격 변경 전 현재 가격을 History에 기록
            StockPriceHistory history =
                    new StockPriceHistory(
                            stock.getStockId(),
                            currentPrice
                    );

            stockPriceHistoryRepository.save(history);

            // 7. Stock 현재 가격 변경
            stock.updatePrice(newPrice);
        }
    }

    private double getTrendEffect(String trend) {

        if (trend == null) {
            return 0.0;
        }

        return switch (trend.toUpperCase()) {
            case "UP" -> 0.003;
            case "DOWN" -> -0.003;
            default -> 0.0;
        };
    }
}