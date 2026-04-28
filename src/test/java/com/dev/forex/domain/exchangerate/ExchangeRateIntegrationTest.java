package com.dev.forex.domain.exchangerate;

import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.domain.exchangerate.repository.ExchangeRateHistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @BeforeEach
    void setUp() {
        exchangeRateHistoryRepository.saveAll(List.of(
                ExchangeRateHistory.builder().currency(CurrencyType.USD).tradeStanRate(new BigDecimal("1477.10")).build(),
                ExchangeRateHistory.builder().currency(CurrencyType.JPY).tradeStanRate(new BigDecimal("926.26")).build(),
                ExchangeRateHistory.builder().currency(CurrencyType.CNY).tradeStanRate(new BigDecimal("216.50")).build(),
                ExchangeRateHistory.builder().currency(CurrencyType.EUR).tradeStanRate(new BigDecimal("1730.10")).build()
        ));
    }

    @AfterEach
    void tearDown() {
        exchangeRateHistoryRepository.deleteAll();
    }

    @Test
    void 전체_환율_조회() throws Exception {
        mockMvc.perform(get("/exchange-rate/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.returnObject.exchangeRateList").isArray())
                .andExpect(jsonPath("$.returnObject.exchangeRateList.length()").value(4));
    }

    @Test
    void 특정_통화_환율_조회() throws Exception {
        mockMvc.perform(get("/exchange-rate/latest/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.returnObject.currency").value("USD"))
                .andExpect(jsonPath("$.returnObject.buyRate").value(1550.96))
                .andExpect(jsonPath("$.returnObject.tradeStanRate").value(1477.10))
                .andExpect(jsonPath("$.returnObject.sellRate").value(1403.25));
    }

    @Test
    void 지원하지_않는_통화_조회() throws Exception {
        mockMvc.perform(get("/exchange-rate/latest/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }
}