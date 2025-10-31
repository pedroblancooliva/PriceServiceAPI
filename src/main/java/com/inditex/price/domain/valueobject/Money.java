package com.inditex.price.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing a monetary amount with currency.
 * Immutable and contains business logic for monetary operations.
 */
public final class Money {
    
    private final BigDecimal amount;
    private final String currency;
    
    public Money(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.amount = amount;
        this.currency = currency.trim().toUpperCase();
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    /**
     * Check if this money has the same currency as another
     */
    public boolean hasSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }
    
    /**
     * Add another money amount (same currency only)
     */
    public Money add(Money other) {
        if (!hasSameCurrency(other)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
    
    @Override
    public String toString() {
        return amount + " " + currency;
    }
}