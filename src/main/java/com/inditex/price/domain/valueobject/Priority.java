package com.inditex.price.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing a priority level.
 * Immutable and contains business logic for priority comparison.
 */
public final class Priority {
    
    private final Integer value;
    
    public Priority(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Priority must be a non-negative integer");
        }
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
    
    /**
     * Check if this priority is higher than another
     */
    public boolean isHigherThan(Priority other) {
        return this.value > other.value;
    }
    
    /**
     * Check if this priority is lower than another
     */
    public boolean isLowerThan(Priority other) {
        return this.value < other.value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Priority priority = (Priority) obj;
        return Objects.equals(value, priority.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "Priority{" + value + "}";
    }
}