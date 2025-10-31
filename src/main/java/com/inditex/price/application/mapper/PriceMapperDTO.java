package com.inditex.price.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.domain.model.Price;
import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;

/**
 * Mapper que convierte entre entidades de dominio y DTOs de aplicación Facilita
 * la separación entre capas
 */

@Mapper
public interface PriceMapperDTO {

	PriceMapperDTO INSTANCE = Mappers.getMapper(PriceMapperDTO.class);

	/**
	 * Convierte una entidad Price a un DTO de respuesta
	 */

	public PriceQueryResponseDTO toResponseDTO(Price price);

}