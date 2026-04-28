package com.dev.forex.domain.exchangerate.dto;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExchangeRateResponse(
        String currency,
        BigDecimal buyRate,
        BigDecimal tradeStanRate,
        BigDecimal sellRate,
        LocalDateTime dateTime
) {
    public static ExchangeRateResponse from(ExchangeRateHistory history) {
        return new ExchangeRateResponse(
                history.getCurrency().name(),
                history.getBuyRate(),
                history.getTradeStanRate(),
                history.getSellRate(),
                history.getCollectedAt()
        );
    }
}