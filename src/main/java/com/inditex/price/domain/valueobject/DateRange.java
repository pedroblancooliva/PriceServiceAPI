package com.inditex.price.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object representing a date range period.
 * Immutable and contains business logic for date validation.
 */
public final class DateRange {
    
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    
    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    /**
     * Check if a given date is within this date range (inclusive)
     */
    public boolean contains(LocalDateTime date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * Check if this date range overlaps with another
     */
    public boolean overlaps(DateRange other) {
        return !this.endDate.isBefore(other.startDate) && !other.endDate.isBefore(this.startDate);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DateRange dateRange = (DateRange) obj;
        return Objects.equals(startDate, dateRange.startDate) && Objects.equals(endDate, dateRange.endDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
    
    @Override
    public String toString() {
        return "DateRange{" + startDate + " to " + endDate + "}";
    }
}