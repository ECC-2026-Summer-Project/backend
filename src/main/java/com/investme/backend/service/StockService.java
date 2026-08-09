package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.dto.StockChartResponse;
import com.investme.backend.dto.StockSummaryResponse;
import com.investme.backend.exception.ApiException;
import com.investme.backend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private static final List<String> VALID_SORTS =
            List.of("price", "changeRate", "volume");

    public Page<Stock> getStockList(
            String keyword,
            String sector,
            String sort,
            String order,
            int page,
            int size
    ) {
        if (!VALID_SORTS.contains(sort)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_SORT",
                    "sort 값이 올바르지 않습니다."
            );
        }

        Sort.Direction direction =
                "asc".equalsIgnoreCase(order)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        String sortField =
                "price".equals(sort)
                        ? "currentPrice"
                        : sort;

        Pageable pageable =
                PageRequest.of(
                        page - 1,
                        size,
                        Sort.by(direction, sortField)
                );

        return stockRepository.search(
                keyword,
                sector,
                pageable
        );
    }

    public StockSummaryResponse getStockSummary(String stockId) {

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "STOCK_NOT_FOUND",
                                "존재하지 않는 종목입니다."
                        )
                );

        int currentPrice = stock.getCurrentPrice();

        int openPrice = currentPrice - stock.getChangeAmount();
        int highPrice = Math.max(currentPrice, openPrice);
        int lowPrice = Math.min(currentPrice, openPrice);

        boolean isAiRecommended = false;

        return new StockSummaryResponse(
                stock.getStockId(),
                stock.getName(),
                stock.getMarket(),
                currentPrice,
                stock.getChangeRate(),
                stock.getChangeAmount(),
                openPrice,
                highPrice,
                lowPrice,
                stock.getVolume(),
                isAiRecommended
        );
    }

    public List<StockChartResponse> getStockChart(
            String stockId,
            String interval,
            String range
    ) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "STOCK_NOT_FOUND",
                                "존재하지 않는 종목입니다."
                        )
                );

        validateChartParameter(interval, range);

        /*
         * 현재 실제 OHLC 데이터가 없기 때문에
         * API 동작 확인을 위한 임시 데이터를 생성한다.
         *
         * 나중에 실제 차트 데이터가 연결되면
         * 이 부분만 실제 데이터 조회 코드로 변경하면 된다.
         */

        int currentPrice = stock.getCurrentPrice();

        List<StockChartResponse> response = new ArrayList<>();

        int dataCount = getDataCount(interval, range);

        for (int i = dataCount - 1; i >= 0; i--) {

            LocalDateTime time = getChartTime(interval, i);

            long open = currentPrice - 700L + (i * 10L);
            long high = open + 500L;
            long low = open - 300L;
            long close = open + 200L;
            long volume = 1000L + (i * 100L);

            response.add(
                    new StockChartResponse(
                            time,
                            open,
                            high,
                            low,
                            close,
                            volume
                    )
            );
        }

        return response;
    }

    private void validateChartParameter(
            String interval,
            String range
    ) {
        if (!List.of("1m", "5m", "1d").contains(interval)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_INTERVAL",
                    "interval 값이 올바르지 않습니다."
            );
        }

        if (!List.of("1d", "1w", "1m").contains(range)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_RANGE",
                    "range 값이 올바르지 않습니다."
            );
        }
    }

    private int getDataCount(
            String interval,
            String range
    ) {
        if ("1d".equals(range)) {
            if ("1m".equals(interval)) {
                return 10;
            }

            if ("5m".equals(interval)) {
                return 10;
            }

            return 10;
        }

        if ("1w".equals(range)) {
            return 10;
        }

        return 10;
    }

    private LocalDateTime getChartTime(
            String interval,
            int index
    ) {
        LocalDateTime now = LocalDateTime.now();

        if ("1m".equals(interval)) {
            return now.minusMinutes(index);
        }

        if ("5m".equals(interval)) {
            return now.minusMinutes(index * 5L);
        }

        return now.minusDays(index);
    }
}