package com.inditex.price.infrastructure.persitence.adapters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;
import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;
import com.inditex.price.infrastructure.persitence.mappers.PriceEntityMapper;
import com.inditex.price.infrastructure.persitence.repositories.PriceJpaRepository;

/**
 * Adaptador que implementa el repositorio de dominio
 * Conecta la capa de dominio con la infraestructura de persistencia
 */
@Repository
public class PriceRepositoryAdapter implements PriceRepository {

    private static final Logger logger = LogManager.getLogger(PriceRepositoryAdapter.class);

    private final PriceJpaRepository priceJpaRepostory;
    private final PriceEntityMapper priceEntityMapper;

    public PriceRepositoryAdapter(PriceJpaRepository priceJpaRepostory, PriceEntityMapper priceEntityMapper) {
        this.priceJpaRepostory = priceJpaRepostory;
        this.priceEntityMapper = priceEntityMapper;

    }

    @Override
    public List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate) {
        logger.debug("Buscando precios aplicables - ProductId: {}, BrandId: {}, Fecha: {}", 
                    productId.getValue(), brandId.getValue(), applicationDate);
        
        long startTime = System.currentTimeMillis();
        
        try {
            List<PriceJpaEntity> priceEntities = priceJpaRepostory.findApplicablePrices(
                    brandId.getValue(),
                    productId.getValue(),
                    applicationDate);

            long queryTime = System.currentTimeMillis() - startTime;
            
            logger.info("Consulta a base de datos completada - ProductId: {}, BrandId: {}, Resultados: {}, Tiempo: {}ms", 
                       productId.getValue(), brandId.getValue(), priceEntities.size(), queryTime);
            
            if (logger.isDebugEnabled() && !priceEntities.isEmpty()) {
                priceEntities.forEach(entity -> 
                    logger.debug("Entidad encontrada - ID: {}, Precio: {}, Lista: {}, Prioridad: {}, Inicio: {}, Fin: {}", 
                               entity.getId(), entity.getPrice(), entity.getPriceList(), 
                               entity.getPriority(), entity.getStartDate(), entity.getEndDate())
                );
            }

            List<Price> domainPrices = priceEntities.stream()
                    .map(priceEntityMapper::toDomain)
                    .collect(Collectors.toList());
            
            long totalTime = System.currentTimeMillis() - startTime;
            
            logger.debug("Mapeo a objetos de dominio completado - Total: {}, Tiempo total: {}ms", 
                        domainPrices.size(), totalTime);
            
            return domainPrices;
            
        } catch (Exception e) {
            long errorTime = System.currentTimeMillis() - startTime;
            logger.error("Error en consulta de precios aplicables - ProductId: {}, BrandId: {}, Fecha: {}, Tiempo: {}ms, Error: {}", 
                        productId.getValue(), brandId.getValue(), applicationDate, errorTime, e.getMessage(), e);
            throw e;
        }
    }
}