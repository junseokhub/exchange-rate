package com.dev.forex.domain.order.dto;

import com.dev.forex.domain.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderListItemResponse(
        Long id,
        BigDecimal fromAmount,
        String fromCurrency,
        BigDecimal toAmount,
        String toCurrency,
        BigDecimal tradeRate,
        LocalDateTime dateTime
) {
    public static OrderListItemResponse from(Order order) {
        return new OrderListItemResponse(
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