package com.dev.forex.domain.exchangerate.entity;

import com.dev.forex.common.entity.BaseEntity;
import com.dev.forex.domain.currency.CurrencyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Entity
@Table(name = "exchange_rate_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRateHistory extends BaseEntity {

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

    //전신환 매입율(buyRate): 매매기준율 × 1.05 (5% 가산)
    //전신환 매도율(sellRate): 매매기준율 × 0.95 (5% 차감)
    private static final BigDecimal BUY_SPREAD_RATE = new BigDecimal("1.05");
    private static final BigDecimal SELL_SPREAD_RATE = new BigDecimal("0.95");

    @Builder
    private ExchangeRateHistory(CurrencyType currency, BigDecimal tradeStanRate) {
        this.currency = currency;
        this.buyRate = tradeStanRate.multiply(BUY_SPREAD_RATE).setScale(2, RoundingMode.HALF_UP);
        this.tradeStanRate = tradeStanRate;
        this.sellRate = tradeStanRate.multiply(SELL_SPREAD_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
