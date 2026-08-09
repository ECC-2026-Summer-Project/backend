package com.investme.backend.controller;

import com.investme.backend.domain.Stock;
import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.StockChartResponse;
import com.investme.backend.dto.StockListItemDto;
import com.investme.backend.dto.StockListResponse;
import com.investme.backend.dto.StockSummaryResponse;
import com.investme.backend.dto.SurgingStockResponse;
import com.investme.backend.service.StockService;
import com.investme.backend.service.SurgingStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final SurgingStockService surgingStockService;

    @GetMapping
    public StockListResponse getStocks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sector,
            @RequestParam(defaultValue = "price") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<Stock> result =
                stockService.getStockList(
                        keyword,
                        sector,
                        sort,
                        order,
                        page,
                        size
                );

        List<StockListItemDto> items =
                result.getContent()
                        .stream()
                        .map(s -> new StockListItemDto(
                                s.getStockId(),
                                s.getName(),
                                s.getMarket(),
                                s.getCurrentPrice(),
                                s.getChangeRate(),
                                s.getChangeAmount(),
                                s.getVolume(),
                                s.getMarketCap()
                        ))
                        .toList();

        return StockListResponse.builder()
                .success(true)
                .data(items)
                .total(result.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

    @GetMapping("/surging")
    public ApiResponse<List<SurgingStockResponse>> getSurgingStocks(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        List<SurgingStockResponse> response =
                surgingStockService.getSurgingStocks(userId);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @GetMapping("/{stockId}/summary")
    public ApiResponse<StockSummaryResponse> getStockSummary(
            @PathVariable String stockId
    ) {
        StockSummaryResponse response =
                stockService.getStockSummary(stockId);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @GetMapping("/{stockId}/chart")
    public ApiResponse<List<StockChartResponse>> getStockChart(
            @PathVariable String stockId,
            @RequestParam(defaultValue = "1d") String interval,
            @RequestParam(defaultValue = "1d") String range
    ) {
        List<StockChartResponse> response =
                stockService.getStockChart(
                        stockId,
                        interval,
                        range
                );

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }
}