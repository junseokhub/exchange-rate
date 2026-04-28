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

    private static final CurrencyType BASE_CURRENCY = CurrencyType.KRW;


    private static BigDecimal calculateKrwAmount(BigDecimal forexAmount, BigDecimal rate, CurrencyType currency) {
        return forexAmount
                .multiply(rate)
                .divide(BigDecimal.valueOf(currency.getBaseUnit()), 0, RoundingMode.FLOOR);
    }

    public static Order buy(CurrencyType toCurrency, BigDecimal forexAmount, BigDecimal buyRate) {
        return Order.builder()
                .fromCurrency(BASE_CURRENCY)
                .toCurrency(toCurrency)
                .fromAmount(calculateKrwAmount(forexAmount, buyRate, toCurrency))
                .toAmount(forexAmount)
                .tradeRate(buyRate)
                .build();
    }

    public static Order sell(CurrencyType fromCurrency, BigDecimal forexAmount, BigDecimal sellRate) {
        return Order.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(BASE_CURRENCY)
                .fromAmount(forexAmount)
                .toAmount(calculateKrwAmount(forexAmount, sellRate, fromCurrency))
                .tradeRate(sellRate)
                .build();
    }
}
