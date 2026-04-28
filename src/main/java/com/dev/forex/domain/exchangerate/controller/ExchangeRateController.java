package com.dev.forex.domain.exchangerate.controller;

import com.dev.forex.common.response.ApiResponse;
import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.dto.ExchangeRateListResponse;
import com.dev.forex.domain.exchangerate.dto.ExchangeRateResponse;
import com.dev.forex.domain.exchangerate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<ExchangeRateListResponse>> getLatestAll() {
        return ResponseEntity.ok(
                ApiResponse.success(ExchangeRateListResponse.from(exchangeRateService.getLatestAll()))
        );
    }

    @GetMapping("/latest/{currency}")
    public ResponseEntity<ApiResponse<ExchangeRateResponse>> getLatestByCurrency(
            @PathVariable String currency) {
        ExchangeRateResponse response = ExchangeRateResponse.from(
                exchangeRateService.getLatestByCurrency(CurrencyType.from(currency)));

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}