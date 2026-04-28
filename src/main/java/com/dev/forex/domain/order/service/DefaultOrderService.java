package com.dev.forex.domain.order.service;

import com.dev.forex.common.exception.BusinessException;
import com.dev.forex.common.exception.ErrorCode;
import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.domain.exchangerate.service.ExchangeRateService;
import com.dev.forex.domain.order.entity.Order;
import com.dev.forex.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final ExchangeRateService exchangeRateService;

    @Override
    @Transactional
    public Order createOrder(BigDecimal forexAmount, String fromCurrency, String toCurrency) {
        CurrencyType from = CurrencyType.from(fromCurrency);
        CurrencyType to = CurrencyType.from(toCurrency);

        validateOrder(from, to);

        CurrencyType foreignCurrency = from == CurrencyType.KRW ? to : from;
        ExchangeRateHistory latestRate = exchangeRateService.getLatestByCurrency(foreignCurrency);

        Order order = from == CurrencyType.KRW
                ? Order.buy(to, forexAmount, latestRate.getBuyRate())
                : Order.sell(from, forexAmount, latestRate.getSellRate());

        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    private void validateOrder(CurrencyType fromCurrency, CurrencyType toCurrency) {
        if (fromCurrency == toCurrency) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_REQUEST);
        }
        if (fromCurrency != CurrencyType.KRW && toCurrency != CurrencyType.KRW) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_REQUEST);
        }
    }
}
