package com.inditex.price.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing a Product identifier.
 * Immutable and contains business logic for product identification.
 */
public final class ProductId {
    
    private final Integer value;
    
    public ProductId(Integer value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive integer");
        }
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductId productId = (ProductId) obj;
        return Objects.equals(value, productId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "ProductId{" + value + "}";
    }
}