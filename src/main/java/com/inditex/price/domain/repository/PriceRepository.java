package com.inditex.price.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Interface del repositorio  Price
 * Define el contrato para las operaciones sobre Price
 */
public interface PriceRepository {
    
    /**
     * Busca los precios a traves del Id de Producto el id de la cadena y la fecha para aplicar
     */
    List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate);
    
    /**
     * Busca por Id
     */
    Optional<Price> findById(Long id);
    
    /**
     * Guarda un Price
     */
    Price save(Price price);
    
    /**
     * Borra por Id
     */
    void deleteById(Long id);
    
    /**
     * Busca todos los precios de un  producto y una cadena especificas
     */
    List<Price> findByProductAndBrand(ProductId productId, BrandId brandId);
}