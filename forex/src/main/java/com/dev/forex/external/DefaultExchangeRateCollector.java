package com.dev.forex.external;

import com.dev.forex.common.exception.BusinessException;
import com.dev.forex.common.exception.ErrorCode;
import com.dev.forex.config.properties.ExternalApiProperties;
import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultExchangeRateCollector implements ExchangeRateCollector{

    private final RestClient restClient;
    private final ExternalApiProperties externalApiProperties;
    private final RetryRegistry retryRegistry;

    @PostConstruct
    public void init() {
        retryRegistry.retry("exchangeRate")
                .getEventPublisher()
                .onRetry(e -> log.warn("외부 API 재시도중: {} 회", e.getNumberOfRetryAttempts()));
    }

    @Override
    @Retry(name = "exchangeRate")
    @CircuitBreaker(name = "exchangeRate", fallbackMethod = "fallback")
    public List<ExchangeRateHistory> collect() {
        ExternalRateResponse response = restClient.get()
                .uri(externalApiProperties.url())
                .retrieve()
                .body(ExternalRateResponse.class);

        if (response == null || !response.isSuccess()) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
        }

        return Arrays.stream(CurrencyType.values())
                .filter(currency -> currency != CurrencyType.KRW)
                .map(currency -> {
                    BigDecimal rawRate = response.getRate(currency.name());
                    BigDecimal tradeStanRate = BigDecimal.ONE
                            .divide(rawRate, 10, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(currency.getBaseUnit()))
                            .setScale(2, RoundingMode.HALF_UP);
                    return ExchangeRateHistory.builder()
                            .currency(currency)
                            .tradeStanRate(tradeStanRate)
                            .build();

                })
                .toList();
    }

    private List<ExchangeRateHistory> fallback(Exception e) {
        log.warn("외부 API 실패로 Mock 데이터로 대체, {}", e.getMessage());
        return Arrays.stream(CurrencyType.values())
                .filter(currency -> currency != CurrencyType.KRW)
                .map(currency -> ExchangeRateHistory.builder()
                        .currency(currency)
                        .tradeStanRate(mockRateData(currency))
                        .build())
                .toList();
    }

    private BigDecimal mockRateData(CurrencyType currency) {
        // 26년 4월 27일 기준 +- 0.5 범위
        return switch (currency) {
            case USD -> BigDecimal.valueOf(1470 + (Math.random() - 0.5) * 100);
            case JPY -> BigDecimal.valueOf(924 + (Math.random() - 0.5) * 100);
            case CNY -> BigDecimal.valueOf(215 + (Math.random() - 0.5) * 100);
            case EUR -> BigDecimal.valueOf(1727 + (Math.random() - 0.5) * 100);
            default -> throw new BusinessException(ErrorCode.CURRENCY_NOT_SUPPORTED);
        };
    }
}
