package com.inditex.price.application.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Mapper que convierte entre entidades de dominio y DTOs de aplicación Facilita
 * la separación entre capas
 */

@Mapper(componentModel = "spring")
public interface PriceMapperDTO {
    PriceMapperDTO INSTANCE = Mappers.getMapper(PriceMapperDTO.class);

    /**
     * Convierte una entidad Price a un DTO de respuesta
     */

    @Mapping(target = "productId", source = "price.productId.value")
    @Mapping(target = "brandId", source = "price.brandId.value")
    @Mapping(target = "price", source = "price.price.amount")
    @Mapping(target = "currency", source = "price.price.currency")
    public PriceQueryResponseDTO toResponseDTO(Price price);

}