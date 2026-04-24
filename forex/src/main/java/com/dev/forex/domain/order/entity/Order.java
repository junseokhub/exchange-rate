package com.dev.forex.domain.order.entity;

import com.dev.forex.common.entity.BaseEntity;
import com.dev.forex.domain.currency.CurrencyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

}
