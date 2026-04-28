package com.dev.forex.domain.exchangerate.dto;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;

import java.util.List;

public record ExchangeRateListResponse(
        List<ExchangeRateResponse> exchangeRateList
) {
    public static ExchangeRateListResponse from(List<ExchangeRateHistory> histories) {
        return new ExchangeRateListResponse(
                histories.stream()
                        .map(ExchangeRateResponse::from)
                        .toList()
        );
    }
}