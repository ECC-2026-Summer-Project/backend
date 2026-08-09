package com.investme.backend.service;

import com.investme.backend.domain.Stock;
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
    private static final List<String> VALID_SORTS = List.of("price", "changeRate", "volume");

    public Page<Stock> getStockList(String keyword, String sector, String sort, String order, int page, int size) {
        if (!VALID_SORTS.contains(sort)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_SORT", "sort 값이 올바르지 않습니다.");
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = "price".equals(sort) ? "currentPrice" : sort;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortField));
        return stockRepository.search(keyword, sector, pageable);
    }
}
