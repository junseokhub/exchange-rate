package com.dev.forex.external;

import com.dev.forex.common.exception.BusinessException;
import com.dev.forex.common.exception.ErrorCode;
import com.dev.forex.config.properties.ExternalApiProperties;
import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultExchangeRateCollector implements ExchangeRateCollector{

    private final WebClient webClient;
    private final ExternalApiProperties externalApiProperties;

    @Override
    public List<ExchangeRateHistory> collect() {
        ExternalRateResponse response = webClient.get()
                .uri(externalApiProperties.url())
                .retrieve()
                .bodyToMono(ExternalRateResponse.class)
                .timeout(Duration.ofSeconds(5))
                .block();

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
}
