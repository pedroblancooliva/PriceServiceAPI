package com.inditex.price.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Repositorio del dominio para Price (Puerto)
 * Define el contrato para el acceso a datos sin depender de la implementación
 */
public interface PriceRepository {

    /**
     * Busca precios aplicables para un producto de una marca en una fecha
     * específica
     * 
     * @param brandId         identificador de la marca/cadena
     * @param productId       identificador del producto
     * @param applicationDate fecha de aplicación del precio
     * @return lista de precios aplicables
     */
    List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate);

}