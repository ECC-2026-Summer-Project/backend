package com.investme.backend.service;

import com.investme.backend.dto.SurgingStockResponse;
import com.investme.backend.entity.Company;
import com.investme.backend.entity.User;
import com.investme.backend.entity.UserActionLog;
import com.investme.backend.repository.CompanyRepository;
import com.investme.backend.repository.UserActionLogRepository;
import com.investme.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurgingStockService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserActionLogRepository userActionLogRepository;

    @Transactional
    public List<SurgingStockResponse> getSurgingStocks(
            String loginUserId
    ) {

        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        List<Company> companies =
                companyRepository.findAll();

        List<Company> surgingCompanies =
                new ArrayList<>();

        // 1. 시작가 대비 10% 이상 상승한 종목 선별
        for (Company company : companies) {

            int basePrice = company.getBasePrice();
            int currentPrice = company.getCurrentPrice();

            if (basePrice <= 0) {
                continue;
            }

            double changeRate =
                    (double) (currentPrice - basePrice)
                            / basePrice
                            * 100;

            if (changeRate >= 10.0) {
                surgingCompanies.add(company);
            }
        }

        // 2. 급등주를 랜덤으로 섞기
        Collections.shuffle(surgingCompanies);

        // 3. 최대 3개 선택
        List<Company> selectedCompanies =
                surgingCompanies.stream()
                        .limit(3)
                        .toList();

        List<SurgingStockResponse> response =
                new ArrayList<>();

        // 4. 응답 생성 + 노출 시각 기록
        for (Company company : selectedCompanies) {

            int basePrice = company.getBasePrice();
            int currentPrice = company.getCurrentPrice();

            int priceChange =
                    currentPrice - basePrice;

            double changeRate =
                    (double) priceChange
                            / basePrice
                            * 100;

UserActionLog exposure =
        new UserActionLog(
                user.getId(),
                company.getCompanyId(),
                "EXPOSURE",
                "SURGING_STOCK",
                null
        );

UserActionLog savedExposure =
        userActionLogRepository.save(exposure);

response.add(
        new SurgingStockResponse(
                savedExposure.getActionId(),
                company.getCompanyId(),
                company.getCompanyName(),
                currentPrice,
                priceChange,
                round(changeRate)
        )
);
        }

        return response;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}