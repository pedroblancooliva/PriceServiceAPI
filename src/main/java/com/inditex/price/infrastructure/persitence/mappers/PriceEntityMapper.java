package com.inditex.price.infrastructure.persitence.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.inditex.price.domain.model.Price;
import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;

/**
 * Mapper que convierte entre entidades JPA y entidades de dominio
 * Mantiene la separación entre la infraestructura y el dominio
 */
@Mapper
public interface PriceEntityMapper {
    
	PriceEntityMapper INSTANCE = Mappers.getMapper(PriceEntityMapper.class);
	
    /**
     * Convierte la entidad JPA  a la entidad de dominio
     */
    public Price toDomain(PriceJpaEntity jpaEntity);

    
    /**
     * Convierte la entidad de dominio a la entidad JPA
     */
    public PriceJpaEntity toJpaEntity(Price domainEntity);
}