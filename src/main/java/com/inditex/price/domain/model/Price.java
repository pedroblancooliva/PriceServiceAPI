package com.inditex.price.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.DateRange;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;


/**
 * Domain Entity representing a Price.
 * Contains business logic and domain rules for pricing.
 * This is the heart of our domain model.
 */
public class Price {
    
    private final Long id;
    private final BrandId brandId;
    private final ProductId productId;
    private final Integer priceList;
    private final DateRange validityPeriod;
    private final Priority priority;
    private final Money price;
    
    public Price(Long id, BrandId brandId, ProductId productId, Integer priceList, 
                 DateRange validityPeriod, Priority priority, Money price) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "Brand ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.priceList = Objects.requireNonNull(priceList, "Price list cannot be null");
        this.validityPeriod = Objects.requireNonNull(validityPeriod, "Validity period cannot be null");
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        
        if (priceList <= 0) {
            throw new IllegalArgumentException("Price list must be positive");
        }
    }
    
    /**
     * Business logic: Check if this price is applicable for the given criteria and date
     */
    public boolean isApplicableFor(ProductId productId, BrandId brandId, LocalDateTime applicationDate) {
        return this.productId.equals(productId) 
            && this.brandId.equals(brandId)
            && this.validityPeriod.contains(applicationDate);
    }
    
    /**
     * Business logic: Check if this price has higher priority than another
     */
    public boolean hasHigherPriorityThan(Price other) {
        return this.priority.isHigherThan(other.priority);
    }
    
    /**
     * Business logic: Check if this price is valid at a specific date
     */
    public boolean isValidAt(LocalDateTime date) {
        return validityPeriod.contains(date);
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public BrandId getBrandId() {
        return brandId;
    }
    
    public ProductId getProductId() {
        return productId;
    }
    
    public Integer getPriceList() {
        return priceList;
    }
    
    public DateRange getValidityPeriod() {
        return validityPeriod;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public Money getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Price price = (Price) obj;
        return Objects.equals(id, price.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", productId=" + productId +
                ", priceList=" + priceList +
                ", validityPeriod=" + validityPeriod +
                ", priority=" + priority +
                ", price=" + price +
                '}';
    }
}