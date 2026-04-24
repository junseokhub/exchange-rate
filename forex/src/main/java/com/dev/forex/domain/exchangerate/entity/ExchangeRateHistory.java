package com.dev.forex.domain.exchangerate.entity;

import com.dev.forex.domain.currency.CurrencyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "exchange_rate_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal buyRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tradeStanRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sellRate;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    //전신환 매입율(buyRate): 매매기준율 × 1.05 (5% 가산)
    //전신환 매도율(sellRate): 매매기준율 × 0.95 (5% 차감)
    @Builder
    private ExchangeRateHistory(CurrencyType currency, BigDecimal tradeStanRate) {
        this.currency = currency;
        this.buyRate = buyRate.multiply(new BigDecimal("1.05")).setScale(2, RoundingMode.HALF_UP);;
        this.tradeStanRate = tradeStanRate;
        this.sellRate = sellRate.multiply(new BigDecimal("0.95")).setScale(2, RoundingMode.HALF_UP);;
        this.collectedAt = LocalDateTime.now();
    }

}
