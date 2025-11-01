package com.inditex.price.domain.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    
    private static final Logger logger = LogManager.getLogger(PriceDomainService.class);
    
    /**
     * Selecciona el precio con mayor prioridad de una lista de precios aplicables
     * Implementa la regla de negocio de desambiguación por prioridad
     * 
     * @param applicablePrices lista de precios aplicables
     * @return el precio con mayor prioridad, si existe
     */
    public Optional<Price> selectHighestPriorityPrice(List<Price> applicablePrices) {
        logger.debug("Iniciando selección de precio con mayor prioridad. Precios disponibles: {}", 
                    applicablePrices != null ? applicablePrices.size() : 0);
        
        if (applicablePrices == null || applicablePrices.isEmpty()) {
            logger.debug("Lista de precios vacía o nula, retornando Optional.empty()");
            return Optional.empty();
        }
        
        logger.debug("Analizando {} precios para determinar el de mayor prioridad", applicablePrices.size());
        
        // Log de todos los precios candidatos
        if (logger.isDebugEnabled()) {
            applicablePrices.forEach(price -> 
                logger.debug("Precio candidato - ID: {}, Prioridad: {}, Precio: {}, Lista: {}", 
                           price.getId(), price.getPriority(), price.getPrice().getAmount(), price.getPriceList())
            );
        }
        
        Optional<Price> selectedPrice = applicablePrices.stream()
                .reduce((price1, price2) -> {
                    logger.debug("Comparando precios - P1[ID:{}, Prioridad:{}] vs P2[ID:{}, Prioridad:{}]", 
                               price1.getId(), price1.getPriority(), price2.getId(), price2.getPriority());
                    
                    Price winner = price1.hasHigherPriorityThan(price2) ? price1 : price2;
                    
                    logger.debug("Ganador de la comparación: ID:{}, Prioridad:{}", 
                               winner.getId(), winner.getPriority());
                    
                    return winner;
                });
        
        if (selectedPrice.isPresent()) {
            Price finalPrice = selectedPrice.get();
            logger.info("Precio seleccionado con mayor prioridad - ID: {}, Prioridad: {}, Precio: {}, Lista: {}, Total candidatos: {}", 
                       finalPrice.getId(), finalPrice.getPriority(), 
                       finalPrice.getPrice().getAmount(), finalPrice.getPriceList(), applicablePrices.size());
        } else {
            logger.warn("No se pudo seleccionar ningún precio después del análisis de prioridades - Total candidatos: {}", 
                       applicablePrices.size());
        }
        
        return selectedPrice;
    }
}