package com.inditex.price.domain.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.inditex.price.domain.model.Price;
import java.util.Comparator;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio que contiene lógica de negocio
 * 
 */
@Service
public class PriceDomainService {

    private static final Logger logger = LogManager.getLogger(PriceDomainService.class);

    /**
     * Selecciona el precio con mayor prioridad
     * 
     * @param applicablePrices lista de precios
     * @return el precio con mayor prioridad, si existe
     */
    public Optional<Price> selectHighestPriorityPrice(List<Price> applicablePrices) {

        if (applicablePrices == null || applicablePrices.isEmpty()) {
            logger.warn("Lista de precios vacía o nula, retornando Optional.empty()");
            return Optional.empty();
        }

        // Si hay empate en prioridad, se selecciona el más reciente (fecha inicio más
        // tardía)
        Optional<Price> selectedPrice = applicablePrices.stream()
                .max(Comparator.comparingInt((Price price) -> price.getPriority().getValue())
                        .thenComparing(price -> price.getStartDate()));
        logger.info("Precio seleccionado con mayor prioridad: {}", selectedPrice.orElse(null));
        return selectedPrice;
    }
}