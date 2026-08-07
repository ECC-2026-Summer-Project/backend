package com.investme.backend.service;

import com.investme.backend.dto.stock.StockSummaryResponse;
import com.investme.backend.entity.Company;
import com.investme.backend.entity.CompanyInfo;
import com.investme.backend.exception.StockNotFoundException;
import com.investme.backend.repository.CompanyInfoRepository;
import com.investme.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(readOnly = true)
public class StockService {

    private final CompanyRepository companyRepository;
    private final CompanyInfoRepository companyInfoRepository;

    public StockService(
            CompanyRepository companyRepository,
            CompanyInfoRepository companyInfoRepository
    ) {
        this.companyRepository = companyRepository;
        this.companyInfoRepository = companyInfoRepository;
    }

    public StockSummaryResponse getSummary(String stockId) {
        Company company = companyRepository.findByCompanyId(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));

        CompanyInfo companyInfo = companyInfoRepository.findByCompanyId(stockId).orElse(null);

        long currentPrice = company.getCurrentPrice();

        // ⚠️ 임시 로직: 실제 알고리즘이 붙기 전까지 현재가 기준으로 변동값을 흉내냄
        Random random = new Random();
        long openPrice = currentPrice - random.nextInt(1000);
        long highPrice = currentPrice + random.nextInt(500);
        long lowPrice = currentPrice - random.nextInt(500);
        long changeAmount = currentPrice - openPrice;
        double changeRate = openPrice == 0 ? 0.0 : Math.round((changeAmount / (double) openPrice) * 10000) / 100.0;
        long volume = 1_000_000L + random.nextInt(20_000_000);

        return StockSummaryResponse.builder()
                .stockId(company.getCompanyId())
                .name(company.getCompanyName())
                .market(companyInfo != null ? companyInfo.getMarket() : null)
                .currentPrice(currentPrice)
                .changeRate(changeRate)
                .changeAmount(changeAmount)
                .openPrice(openPrice)
                .highPrice(highPrice)
                .lowPrice(lowPrice)
                .volume(volume)
                .isAiRecommended(companyInfo != null ? companyInfo.getIsAiRecommended() : false)
                .build();
    }
}