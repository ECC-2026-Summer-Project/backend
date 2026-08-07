package com.investme.backend.service;

import com.investme.backend.dto.stock.StockChartResponse;
import com.investme.backend.dto.stock.StockOrderBookResponse;
import com.investme.backend.dto.stock.StockSummaryResponse;
import com.investme.backend.entity.Company;
import com.investme.backend.entity.CompanyInfo;
import com.investme.backend.exception.StockNotFoundException;
import com.investme.backend.repository.CompanyInfoRepository;
import com.investme.backend.repository.CompanyRepository;
import com.investme.backend.repository.DividendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.investme.backend.dto.stock.StockTradeResponse;
import com.investme.backend.entity.TradeHistory;
import com.investme.backend.repository.TradeHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.investme.backend.dto.stock.StockDividendResponse;
import com.investme.backend.entity.Dividend;
import com.investme.backend.dto.stock.StockInfoResponse;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StockService {

    private final CompanyRepository companyRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final TradeHistoryRepository tradeHistoryRepository;
    private final DividendRepository dividendRepository;

    public StockService(
            CompanyRepository companyRepository,
            CompanyInfoRepository companyInfoRepository,
            TradeHistoryRepository tradeHistoryRepository,
            DividendRepository dividendRepository
    ) {
        this.companyRepository = companyRepository;
        this.companyInfoRepository = companyInfoRepository;
        this.tradeHistoryRepository = tradeHistoryRepository;
        this.dividendRepository = dividendRepository;
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

    public java.util.List<StockChartResponse> getChart(String stockId, String interval, String range) {
        Company company = companyRepository.findByCompanyId(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));

        long currentPrice = company.getCurrentPrice();

        int candleCount = resolveCandleCount(interval, range);
        long minutesPerCandle = resolveMinutesPerCandle(interval);

        java.util.List<StockChartResponse> chartData = new java.util.ArrayList<>();
        Random random = new Random();

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        long basePrice = currentPrice;

        for (int i = candleCount - 1; i >= 0; i--) {
            java.time.LocalDateTime time = now.minusMinutes(minutesPerCandle * i);

            long open = basePrice - 500 + random.nextInt(1000);
            long close = basePrice - 500 + random.nextInt(1000);
            long high = Math.max(open, close) + random.nextInt(300);
            long low = Math.min(open, close) - random.nextInt(300);
            long volume = 1000 + random.nextInt(5000);

            chartData.add(StockChartResponse.builder()
                    .time(time)
                    .open(open)
                    .high(high)
                    .low(low)
                    .close(close)
                    .volume(volume)
                    .build());
        }

        return chartData;
    }

    private int resolveCandleCount(String interval, String range) {
        if ("1d".equals(range) && "1m".equals(interval)) return 60;
        if ("1d".equals(range) && "5m".equals(interval)) return 30;
        if ("1d".equals(range) && "1d".equals(interval)) return 1;
        if ("1w".equals(range) && "1d".equals(interval)) return 7;
        if ("1m".equals(range) && "1d".equals(interval)) return 30;
        // 정의되지 않은 조합은 기본값(1d 기준 1개)으로 처리
        return 1;
    }

    private long resolveMinutesPerCandle(String interval) {
        return switch (interval) {
            case "1m" -> 1L;
            case "5m" -> 5L;
            case "1d" -> 24 * 60L;
            default -> 24 * 60L;
        };
    }

    public StockOrderBookResponse getOrderBook(String stockId) {

        Company company = companyRepository.findByCompanyId(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));

        long currentPrice = company.getCurrentPrice();

        Random random = new Random();

        List<StockOrderBookResponse.OrderBookLevel> asks = new ArrayList<>();
        List<StockOrderBookResponse.OrderBookLevel> bids = new ArrayList<>();

        // 매도호가 5단계
        for (int i = 1; i <= 5; i++) {
            long price = currentPrice + (100L * i);
            long quantity = 100L + random.nextInt(900);

            asks.add(
                    StockOrderBookResponse.OrderBookLevel.builder()
                            .price(price)
                            .quantity(quantity)
                            .build()
            );
        }

        // 매수호가 5단계
        for (int i = 1; i <= 5; i++) {
            long price = currentPrice - (100L * i);
            long quantity = 100L + random.nextInt(900);

            bids.add(
                    StockOrderBookResponse.OrderBookLevel.builder()
                            .price(price)
                            .quantity(quantity)
                            .build()
            );
        }

        return StockOrderBookResponse.builder()
                .currentPrice(currentPrice)
                .asks(asks)
                .bids(bids)
                .build();
    }

    public List<StockTradeResponse> getTrades(String stockId, int limit) {

        // 존재하지 않는 종목인지 먼저 확인
        companyRepository.findByCompanyId(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));

        Pageable pageable = PageRequest.of(0, limit);

        List<TradeHistory> tradeHistories =
                tradeHistoryRepository
                        .findByCompany_CompanyIdOrderByTradedAtDesc(
                                stockId,
                                pageable
                        );

        return tradeHistories.stream()
                .map(trade -> StockTradeResponse.builder()
                        .time(trade.getTradedAt())
                        .price(trade.getPrice())
                        .quantity(trade.getQuantity())
                        .side(trade.getTradeType())
                        .build()
                )
                .toList();
    }

    public List<StockDividendResponse> getDividends(String stockId) {

        // 존재하지 않는 종목인지 확인
        companyRepository.findByCompanyId(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));

        List<Dividend> dividends =
                dividendRepository.findByCompany_CompanyIdOrderByYearDesc(stockId);

        return dividends.stream()
                .map(dividend -> StockDividendResponse.builder()
                        .year(dividend.getYear())
                        .amountPerShare(dividend.getAmountPerShare())
                        .yieldRate(dividend.getYieldRate())
                        .build()
                )
                .toList();
    }

    public StockInfoResponse getInfo(String stockId) {

        Company company = companyRepository.findByCompanyId(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));

        return StockInfoResponse.builder()
                .stockId(company.getCompanyId())
                .name(company.getCompanyName())
                .description(null)
                .industry(company.getSector())
                .marketCap(null)
                .per(null)
                .dividendYield(company.getDividendYield())
                .listedDate(null)
                .ceo(null)
                .employees(null)
                .build();
    }


}