package com.dev.forex.domain.order.dto;

import com.dev.forex.domain.order.entity.Order;

import java.util.List;

public record OrderListResponse(
        List<OrderListItemResponse> orderList
) {
    public static OrderListResponse from(List<Order> orders) {
        return new OrderListResponse(
                orders.stream()
                        .map(OrderListItemResponse::from)
                        .toList()
        );
    }
}