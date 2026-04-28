package com.dev.forex.domain.order.service;

import com.dev.forex.domain.order.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order createOrder(BigDecimal forexAmount, String fromCurrency, String toCurrency);
    List<Order> getOrderList();
}
