package com.dev.forex.domain.order;

import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.domain.exchangerate.repository.ExchangeRateHistoryRepository;
import com.dev.forex.domain.order.dto.OrderRequest;
import com.dev.forex.domain.order.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
        orderRepository.deleteAll();
        exchangeRateHistoryRepository.deleteAll();
    }

    @Test
    void KRW에서_외화_매수_주문() throws Exception {
        String request = objectMapper.writeValueAsString(
                new OrderRequest(new BigDecimal("200"), "KRW", "USD"));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.returnObject.fromCurrency").value("KRW"))
                .andExpect(jsonPath("$.returnObject.toCurrency").value("USD"))
                .andExpect(jsonPath("$.returnObject.toAmount").value(200))
                .andExpect(jsonPath("$.returnObject.fromAmount").value(310192));
    }

    @Test
    void 외화에서_KRW_매도_주문() throws Exception {
        String request = objectMapper.writeValueAsString(
                new OrderRequest(new BigDecimal("133"), "USD", "KRW"));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.returnObject.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.returnObject.toCurrency").value("KRW"))
                .andExpect(jsonPath("$.returnObject.fromAmount").value(133))
                .andExpect(jsonPath("$.returnObject.toAmount").value(186632));
    }

    @Test
    void 동일_통화_주문_실패() throws Exception {
        String request = objectMapper.writeValueAsString(
                new OrderRequest(new BigDecimal("200"), "KRW", "KRW"));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void 이중통화_주문_실패() throws Exception {
        String request = objectMapper.writeValueAsString(
                new OrderRequest(new BigDecimal("200"), "USD", "JPY"));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void 환율_데이터_없을때_주문_실패() throws Exception {
        exchangeRateHistoryRepository.deleteAll();

        String request = objectMapper.writeValueAsString(
                new OrderRequest(new BigDecimal("200"), "KRW", "USD"));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void 주문_목록_조회() throws Exception {
        String request = objectMapper.writeValueAsString(
                new OrderRequest(new BigDecimal("200"), "KRW", "USD"));

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        mockMvc.perform(get("/order/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.returnObject.orderList").isArray())
                .andExpect(jsonPath("$.returnObject.orderList.length()").value(1));
    }
}