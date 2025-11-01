package com.inditex.price.domain.model;



import java.time.LocalDateTime;
import java.util.Objects;

import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Entidad de dominio que representa un precio con su tarifa aplicable
 * Contiene toda la lógica de negocio relacionada con los precios
 */
public class Price {
    
    private final Long id;
    private final BrandId brandId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priceList;
    private final ProductId productId;
    private final Priority priority;
    private final Money price;
    
    public Price(Long id, BrandId brandId, LocalDateTime startDate, LocalDateTime endDate,
                 Integer priceList, ProductId productId, Priority priority, Money price) {
        
        validateDates(startDate, endDate);
        validatePriceList(priceList);
        validateRequiredFields(brandId, productId, priority, price);
        
        this.id = id;
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
    }
    
    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
    
    private void validatePriceList(Integer priceList) {
        if (priceList == null || priceList <= 0) {
            throw new IllegalArgumentException("La lista de precios debe ser un número positivo");
        }
    }
    
    private void validateRequiredFields(BrandId brandId, ProductId productId, Priority priority, Money price) {
        if (brandId == null) throw new IllegalArgumentException("Brand ID no puede ser nulo");
        if (productId == null) throw new IllegalArgumentException("Product ID no puede ser nulo");
        if (priority == null) throw new IllegalArgumentException("Priority no puede ser nula");
        if (price == null) throw new IllegalArgumentException("Price no puede ser nulo");
    }
    
    /**
     * Determina si este precio es aplicable en la fecha dada
     */
    public boolean isApplicableAt(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return !dateTime.isBefore(startDate) && !dateTime.isAfter(endDate);
    }
    
    /**
     * Determina si este precio tiene mayor prioridad que otro
     */
    public boolean hasHigherPriorityThan(Price other) {
        return this.priority.isHigherThan(other.priority);
    }
    
    /**
     * Verifica si este precio corresponde al producto y marca dados
     */
    public boolean belongsTo(ProductId productId, BrandId brandId) {
        return this.productId.equals(productId) && this.brandId.equals(brandId);
    }
    
    // Getters
    public Long getId() { return id; }
    public BrandId getBrandId() { return brandId; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Integer getPriceList() { return priceList; }
    public ProductId getProductId() { return productId; }
    public Priority getPriority() { return priority; }
    public Money getPrice() { return price; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
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
                ", priority=" + priority +
                ", price=" + price +
                ", period=" + startDate + " to " + endDate +
                '}';
    }
    
    /**
     * Método estático para crear un Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Patrón Builder para la construcción de objetos Price
     * Proporciona una API fluida y legible para crear instancias
     */
    public static class Builder {
        private Long id;
        private BrandId brandId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer priceList;
        private ProductId productId;
        private Priority priority;
        private Money price;
        
        private Builder() {}
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder brandId(BrandId brandId) {
            this.brandId = brandId;
            return this;
        }
        
        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }
        
        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }
        
        public Builder priceList(Integer priceList) {
            this.priceList = priceList;
            return this;
        }
        
        public Builder productId(ProductId productId) {
            this.productId = productId;
            return this;
        }
        
        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder price(Money price) {
            this.price = price;
            return this;
        }
        
        /**
         * Construye la instancia de Price con todas las validaciones
         */
        public Price build() {
            return new Price(id, brandId, startDate, endDate, priceList, productId, priority, price);
        }
    }
}