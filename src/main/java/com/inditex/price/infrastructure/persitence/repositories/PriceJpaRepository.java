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
     * BUSCA PRECIOS APLICABLES PARA UN PRODUCTO DE UNA MARCA EN UNA FECHA
     * ESPECÍFICA
     * LA FECHA DEBE ESTAR DENTRO DEL RANGO [STARTDATE, ENDDATE]
     * 
     * ORDENAMOS POR PRIORIDAD DESC Y FECHA INICIO DESC
     * POR SI HAY DOS PRECIOS CON LA MISMA PRIORIDAD Y RANGO SOLAPADO,
     * SE APLICARA EL DE FECHA DE INICIO MÁS RECIENTE EN EL SERVICIO
     */
    @Query("SELECT p FROM PriceJpaEntity p " +
            "WHERE p.brandId = :brandId " +
            "AND p.productId = :productId " +
            "AND :applicationDate >= p.startDate " +
            "AND :applicationDate <= p.endDate " +
            "ORDER BY p.priority DESC, p.startDate DESC")
    List<PriceJpaEntity> findApplicablePrices(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate);
}