package com.dev.forex.domain.order.entity;

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
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal fromAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CurrencyType fromCurrency;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal toAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CurrencyType toCurrency;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal tradeRate;

    @Builder
    private Order(CurrencyType fromCurrency, CurrencyType toCurrency, BigDecimal fromAmount, BigDecimal toAmount, BigDecimal tradeRate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.tradeRate = tradeRate;
    }

    public static Order buy(CurrencyType toCurrency, BigDecimal forexAmount, BigDecimal buyRate) {
        BigDecimal krwAmount = forexAmount
                .multiply(buyRate)
                .divide(BigDecimal.valueOf(toCurrency.getBaseUnit()), 0, RoundingMode.FLOOR);

        return Order.builder()
                .fromCurrency(CurrencyType.KRW)
                .toCurrency(toCurrency)
                .fromAmount(krwAmount)
                .toAmount(forexAmount)
                .tradeRate(buyRate)
                .build();
    }

    public static Order sell(CurrencyType fromCurrency, BigDecimal forexAmount, BigDecimal sellRate) {
        BigDecimal krwAmount = forexAmount
                .multiply(sellRate)
                .divide(BigDecimal.valueOf(fromCurrency.getBaseUnit()), 0, RoundingMode.FLOOR);

        return Order.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(CurrencyType.KRW)
                .fromAmount(forexAmount)
                .toAmount(krwAmount)
                .tradeRate(sellRate)
                .build();
    }
}
