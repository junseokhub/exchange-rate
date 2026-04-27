package com.dev.forex.domain.order.dto;

import com.dev.forex.domain.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        BigDecimal fromAmount,
        String fromCurrency,
        BigDecimal toAmount,
        String toCurrency,
        BigDecimal tradeRate,
        LocalDateTime dateTime
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getFromAmount(),
                order.getFromCurrency().name(),
                order.getToAmount(),
                order.getToCurrency().name(),
                order.getTradeRate(),
                order.getCreatedAt()
        );
    }
}