package com.dev.forex.domain.exchangerate.entity;

import com.dev.forex.domain.currency.CurrencyType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateHistoryTest {

    @Test
    void buyRate_1_05() {
        ExchangeRateHistory history = ExchangeRateHistory.builder()
                .currency(CurrencyType.USD)
                .tradeStanRate(new BigDecimal("1000"))
                .build();

        assertThat(history.getBuyRate()).isEqualTo(new BigDecimal("1050.00"));
    }

    @Test
    void sellRate_0_95() {
        ExchangeRateHistory history = ExchangeRateHistory.builder()
                .currency(CurrencyType.USD)
                .tradeStanRate(new BigDecimal("1000"))
                .build();
        assertThat(history.getSellRate()).isEqualTo(new BigDecimal("950.00"));
    }

    @Test
    void 소수점_둘째자리_반올림() {
        ExchangeRateHistory history = ExchangeRateHistory.builder()
                .currency(CurrencyType.USD)
                .tradeStanRate(new BigDecimal("1477.10"))
                .build();

        assertThat(history.getBuyRate()).isEqualTo(new BigDecimal("1550.96"));
        assertThat(history.getSellRate()).isEqualTo(new BigDecimal("1403.25"));
    }

    @Test
    void JPY_100엔_기준_계산() {
        ExchangeRateHistory history = ExchangeRateHistory.builder()
                .currency(CurrencyType.JPY)
                .tradeStanRate(new BigDecimal("926.00"))
                .build();

        assertThat(history.getBuyRate()).isEqualTo(new BigDecimal("972.30"));
        assertThat(history.getSellRate()).isEqualTo(new BigDecimal("879.70"));
    }

}