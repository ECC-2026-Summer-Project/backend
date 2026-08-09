package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.dto.StockSummaryResponse;
import com.investme.backend.exception.ApiException;
import com.investme.backend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

        /*
         * 현재 Stock Entity에는
         * openPrice / highPrice / lowPrice가 없기 때문에
         * API 테스트를 위한 임시값을 사용한다.
         *
         * 나중에 실제 주가 데이터가 연결되면
         * 이 부분만 실제 데이터 조회 코드로 변경하면 된다.
         */
        int openPrice = currentPrice - stock.getChangeAmount();

        int highPrice = Math.max(
                currentPrice,
                openPrice
        );

        int lowPrice = Math.min(
                currentPrice,
                openPrice
        );

        /*
         * 현재 AI 알고리즘과 연결되어 있지 않으므로
         * 테스트용으로 false를 반환한다.
         */
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
}