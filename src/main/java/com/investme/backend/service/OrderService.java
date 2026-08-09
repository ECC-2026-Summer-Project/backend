package com.investme.backend.service;
import com.investme.backend.domain.Stock;
import com.investme.backend.domain.TradeHistory;
import com.investme.backend.entity.UserStock;
import com.investme.backend.dto.OrderRequest;
import com.investme.backend.entity.User;
import com.investme.backend.exception.ApiException;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.repository.TradeHistoryRepository;
import com.investme.backend.repository.UserRepository;
import com.investme.backend.repository.UserStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class OrderService {
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;
    private final TradeHistoryRepository tradeHistoryRepository;
    @Transactional
    public TradeHistory executeOrder(String loginUserId, OrderRequest request) {
        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."));
        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "STOCK_NOT_FOUND", "존재하지 않는 종목입니다."));
        int executedPrice = "LIMIT".equals(request.getOrderType()) ? request.getPrice() : stock.getCurrentPrice();
        long totalAmount = (long) executedPrice * request.getQuantity();
        if ("BUY".equals(request.getSide())) {
            if (user.getCashBalance() < totalAmount) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "INSUFFICIENT_BALANCE", "잔액이 부족합니다.");
            }
            user.setCashBalance(user.getCashBalance() - totalAmount);
            userRepository.save(user);
            UserStock holding = userStockRepository.findByUserIdAndCompanyId(user.getId(), stock.getStockId())
                    .orElse(new UserStock(user.getId(), stock.getStockId(), 0, 0));
            holding.addQuantity(request.getQuantity(), executedPrice);
            userStockRepository.save(holding);
        } else if ("SELL".equals(request.getSide())) {
            UserStock holding = userStockRepository.findByUserIdAndCompanyId(user.getId(), stock.getStockId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "INSUFFICIENT_HOLDING", "보유 수량이 부족합니다."));
            if (holding.getQuantity() < request.getQuantity()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "INSUFFICIENT_HOLDING", "보유 수량이 부족합니다.");
            }
            holding.reduceQuantity(request.getQuantity());
            userStockRepository.save(holding);
            user.setCashBalance(user.getCashBalance() + totalAmount);
            userRepository.save(user);
        }
        TradeHistory trade = new TradeHistory(
                user.getId(), stock.getStockId(), request.getSide(), request.getOrderType(),
                request.getQuantity(), executedPrice, totalAmount, "FILLED", request.getTriggeredByEventId());
        return tradeHistoryRepository.save(trade);
    }
}
