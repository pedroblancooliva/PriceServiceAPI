package com.inditex.price.domain.service;

import org.springframework.stereotype.Service;
import com.inditex.price.domain.model.Price;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio que contiene lógica de negocio
 * que no pertenece a una entidad específica
 */
@Service
public class PriceDomainService {
    
    /**
     * Selecciona el precio con mayor prioridad de una lista de precios aplicables
     * Implementa la regla de negocio de desambiguación por prioridad
     * 
     * @param applicablePrices lista de precios aplicables
     * @return el precio con mayor prioridad, si existe
     */
    public Optional<Price> selectHighestPriorityPrice(List<Price> applicablePrices) {
        if (applicablePrices == null || applicablePrices.isEmpty()) {
            return Optional.empty();
        }
        
        return applicablePrices.stream()
                .reduce((price1, price2) -> 
                    price1.hasHigherPriorityThan(price2) ? price1 : price2
                );
    }
}