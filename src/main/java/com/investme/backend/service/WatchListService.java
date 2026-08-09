package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.domain.WatchList;
import com.investme.backend.dto.WatchListItemDto;
import com.investme.backend.exception.ApiException;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.repository.WatchListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchListService {

    private final WatchListRepository watchListRepository;
    private final StockRepository stockRepository;

    public List<WatchListItemDto> getWatchList(Long userId) {
        return watchListRepository.findByUserId(userId).stream()
                .map(w -> {
                    Stock stock = stockRepository.findById(w.getStockId())
                            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "STOCK_NOT_FOUND", "존재하지 않는 종목입니다."));
                    return new WatchListItemDto(
                            stock.getStockId(), stock.getName(), stock.getCurrentPrice(),
                            stock.getChangeRate(), stock.getChangeAmount(), w.getCreatedAt());
                })
                .toList();
    }

    public WatchList addToWatchList(Long userId, String stockId) {
        if (watchListRepository.findByUserIdAndStockId(userId, stockId).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "ALREADY_EXISTS", "이미 관심 종목에 등록되어 있습니다.");
        }
        return watchListRepository.save(new WatchList(userId, stockId));
    }

    public void removeFromWatchList(Long userId, String stockId) {
        watchListRepository.deleteByUserIdAndStockId(userId, stockId);
    }
}
