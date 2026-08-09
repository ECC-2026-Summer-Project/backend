package com.investme.backend.service;

import com.investme.backend.entity.Company;
import com.investme.backend.entity.StockPriceHistory;
import com.investme.backend.repository.CompanyRepository;
import com.investme.backend.repository.StockPriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockPriceService {

    private final CompanyRepository companyRepository;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;

    @Transactional
    public void updateStockPrices() {

        // 1. 모든 종목 조회
        List<Company> companies = companyRepository.findAll();

        for (Company company : companies) {

            int currentPrice = company.getCurrentPrice();

            // 2. 기업별 변동성 가져오기
            double volatility = company.getVolatility() == null
                    ? 0.02
                    : company.getVolatility();

            // 3. -volatility ~ +volatility 범위의 랜덤 변동 생성
            double randomChange =
                    (Math.random() * 2 - 1) * volatility;

            // 4. 기업의 추세에 따른 추가 변화
            double trendEffect = getTrendEffect(company.getTrend());

            double changeRate =
                    randomChange + trendEffect;

            // 5. 새로운 가격 계산
            int newPrice = (int) Math.round(
                    currentPrice * (1 + changeRate)
            );

            // 가격이 0 이하가 되는 것 방지
            newPrice = Math.max(newPrice, 1);

            // 6. 기존 가격을 History에 기록
            StockPriceHistory history =
                    new StockPriceHistory(
                            company.getCompanyId(),
                            currentPrice
                    );

            stockPriceHistoryRepository.save(history);

            // 7. Company의 현재 가격 변경
            company.updatePrice(newPrice);
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