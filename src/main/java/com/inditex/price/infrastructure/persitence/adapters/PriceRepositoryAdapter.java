package com.inditex.price.infrastructure.persitence.adapters;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;
import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;
import com.inditex.price.infrastructure.persitence.mappers.PriceMapper;
import com.inditex.price.infrastructure.persitence.repositories.PriceJpaRepository;

/**
 * Adaptador que implementa el repositorio de dominio
 * Conecta la capa de dominio con la infraestructura de persistencia
 */
@Repository
public class PriceRepositoryAdapter implements PriceRepository {
    
    private final PriceJpaRepository priceJpaRepostory;
    
    @Autowired
    public PriceRepositoryAdapter(PriceJpaRepository priceJpaRepostory) {
        this.priceJpaRepostory = priceJpaRepostory;

    }
    
    @Override
    public List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate){
        List<PriceJpaEntity> priceEntities = priceJpaRepostory.findApplicablePrices(
                brandId.getValue(),
                productId.getValue(),
                applicationDate
        );
        
        return priceEntities.stream()
                .map(PriceMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }
}