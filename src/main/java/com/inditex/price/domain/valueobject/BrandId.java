package com.inditex.price.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing a Brand identifier.
 * Immutable and contains business logic for brand identification.
 */
public final class BrandId {
    
    private final Long value;
    
    public BrandId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Brand ID must be a positive integer");
        }
        this.value = value;
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BrandId brandId = (BrandId) obj;
        return Objects.equals(value, brandId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "BrandId{" + value + "}";
    }
}