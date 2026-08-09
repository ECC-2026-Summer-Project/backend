package com.investme.backend.controller;

import com.investme.backend.dto.AccountSummaryResponse;
import com.investme.backend.dto.ApiResponse;
import com.investme.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me/summary")
    public ApiResponse<AccountSummaryResponse> getAccountSummary(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        AccountSummaryResponse response =
                accountService.getAccountSummary(userId);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }
}