package com.inditex.price.infrastructure.persitence.mappers;


import org.springframework.stereotype.Component;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;
import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;

/**
 * Mapper que convierte entre entidades JPA y entidades de dominio
 * Mantiene la separación entre la infraestructura y el dominio
 */
@Component
public class PriceEntityMapper {
    
    /**
     * Convierte una entidad JPA a una entidad de dominio
     * Utiliza el patrón Builder para una construcción más legible
     */
    public Price toDomain(PriceJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Price.builder()
                .id(entity.getId())
                .brandId(new BrandId(entity.getBrandId()))
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(new ProductId(entity.getProductId()))
                .priority(new Priority(entity.getPriority()))
                .price(new Money(entity.getPrice(), entity.getCurrency()))
                .build();
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    public PriceJpaEntity toEntity(Price domain) {
        if (domain == null) {
            return null;
        }
        
  
        PriceJpaEntity entity = new PriceJpaEntity(
                domain.getBrandId().getValue(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getPriceList(),
                domain.getProductId().getValue(),
                domain.getPriority().getValue(),
                domain.getPrice().getAmount(),
                domain.getPrice().getCurrency()
        );
        
        // Si la entidad tiene ID, lo establecemos
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        return entity;
    }
}