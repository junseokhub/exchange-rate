package com.dev.forex.domain.order.entity;

import com.dev.forex.domain.currency.CurrencyType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTest {

    @Test
    void KRW_매수_금액() {
        Order order = Order.buy(CurrencyType.USD, new BigDecimal("200"), new BigDecimal("1550.96"));

        assertThat(order.getFromAmount()).isEqualTo(new BigDecimal("310192"));
        assertThat(order.getToAmount()).isEqualTo(new BigDecimal("200"));
        assertThat(order.getFromCurrency()).isEqualTo(CurrencyType.KRW);
        assertThat(order.getToCurrency()).isEqualTo(CurrencyType.USD);
    }

    @Test
    void KRW로_매도_금액() {
        Order order = Order.sell(CurrencyType.USD, new BigDecimal("133"), new BigDecimal("1403.25"));

        assertThat(order.getToAmount()).isEqualTo(new BigDecimal("186632"));
        assertThat(order.getFromAmount()).isEqualTo(new BigDecimal("133"));
        assertThat(order.getFromCurrency()).isEqualTo(CurrencyType.USD);
        assertThat(order.getToCurrency()).isEqualTo(CurrencyType.KRW);
    }

    @Test
    void JPY_매수_100엔_기준() {
        Order order = Order.buy(CurrencyType.JPY, new BigDecimal("200"), new BigDecimal("972.57"));

        assertThat(order.getFromAmount()).isEqualTo(new BigDecimal("1945"));
        assertThat(order.getToAmount()).isEqualTo(new BigDecimal("200"));
    }

    @Test
    void JPY_매도_100엔_기준() {
        Order order = Order.sell(CurrencyType.JPY, new BigDecimal("200"), new BigDecimal("879.95"));

        assertThat(order.getToAmount()).isEqualTo(new BigDecimal("1759"));
        assertThat(order.getFromAmount()).isEqualTo(new BigDecimal("200"));
    }

    @Test
    void KRW_환산_금액_소수점_버림() {
        Order order = Order.sell(CurrencyType.USD, new BigDecimal("1"), new BigDecimal("1403.99"));

        assertThat(order.getToAmount()).isEqualTo(new BigDecimal("1403"));
    }
}