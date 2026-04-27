package com.dev.forex.domain.order.dto;

import com.dev.forex.domain.order.entity.Order;

import java.util.List;

public record OrderListResponse(
        List<OrderResponse> orderList
) {
    public static OrderListResponse from(List<Order> orders) {
        return new OrderListResponse(
                orders.stream()
                        .map(OrderResponse::from)
                        .toList()
        );
    }
}