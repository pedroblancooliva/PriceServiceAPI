package com.inditex.price.infrastructure.persitence.repositories;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;

/**
 * Repositorio JPA para acceso a datos de precios
 * Extiende JpaRepository para operaciones básicas CRUD
 */
@Repository
public interface PriceJpaRepository extends JpaRepository<PriceJpaEntity, Long> {
    
    /**
     * Busca precios aplicables para un producto de una marca en una fecha específica
     * La fecha debe estar dentro del rango [startDate, endDate]
     */
    @Query("SELECT p FROM PriceEntity p " +
           "WHERE p.brandId = :brandId " +
           "AND p.productId = :productId " +
           "AND :applicationDate >= p.startDate " +
           "AND :applicationDate <= p.endDate " +
           "ORDER BY p.priority DESC")
    List<PriceJpaEntity> findApplicablePrices(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}