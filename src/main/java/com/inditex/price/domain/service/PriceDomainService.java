package com.inditex.price.domain.service;



import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Servicio que contiene toda la lógica de negocio de Price
 */
public class PriceDomainService {
    
    private final PriceRepository priceRepository;
    
    public PriceDomainService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }
    
    /**
     * Business logic: Find the applicable price with highest priority
     * for a given product, brand and application date.
     * 
     * Business Rules:
     * 1. The price must be valid for the application date
     * 2. If multiple prices are found, return the one with highest priority
     * 3. If no price is found, return empty
     */
    public Optional<Price> findApplicablePrice(ProductId productId, BrandId brandId, LocalDateTime applicationDate) {
        List<Price> applicablePrices = priceRepository.findApplicablePrices(productId, brandId, applicationDate);
        
        return applicablePrices.stream()
                .filter(price -> price.isApplicableFor(productId, brandId, applicationDate))
                .max(Comparator.comparing(price -> price.getPriority().getValue()));
    }
    
    /**
     * Business logic: Validate if a new price can be created without conflicts
     */
    public boolean canCreatePrice(Price newPrice) {
        List<Price> existingPrices = priceRepository.findByProductAndBrand(
            newPrice.getProductId(), 
            newPrice.getBrandId()
        );
        
        // Business rule: Check for conflicting periods with same priority
        return existingPrices.stream()
                .filter(existing -> existing.getPriority().equals(newPrice.getPriority()))
                .noneMatch(existing -> existing.getValidityPeriod().overlaps(newPrice.getValidityPeriod()));
    }
    
    /**
     * Business logic: Find all conflicting prices for a given price
     */
    public List<Price> findConflictingPrices(Price price) {
        List<Price> existingPrices = priceRepository.findByProductAndBrand(
            price.getProductId(), 
            price.getBrandId()
        );
        
        return existingPrices.stream()
                .filter(existing -> !existing.equals(price))
                .filter(existing -> existing.getPriority().equals(price.getPriority()))
                .filter(existing -> existing.getValidityPeriod().overlaps(price.getValidityPeriod()))
                .collect(java.util.stream.Collectors.toList());
    }
}