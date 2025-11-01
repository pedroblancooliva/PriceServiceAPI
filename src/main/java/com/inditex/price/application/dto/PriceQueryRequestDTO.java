package com.inditex.price.application.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

/**
 * DTO para la consulta de precios
 * Representa los parámetros de entrada del endpoint
 */
public class PriceQueryRequestDTO {
    
    @NotNull(message = "La fecha de aplicación es obligatoria")
    private LocalDateTime applicationDate;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;
    
    @NotNull(message = "El ID de la marca es obligatorio")
    private Long brandId;
    
    public PriceQueryRequestDTO() {}
    
    public PriceQueryRequestDTO(LocalDateTime applicationDate, Long productId, Long brandId) {
        this.applicationDate = applicationDate;
        this.productId = productId;
        this.brandId = brandId;
    }
    
    // Getters y setters
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }
    
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    
    @Override
    public String toString() {
        return "PriceQueryRequest{" +
                "applicationDate=" + applicationDate +
                ", productId=" + productId +
                ", brandId=" + brandId +
                '}';
    }
}