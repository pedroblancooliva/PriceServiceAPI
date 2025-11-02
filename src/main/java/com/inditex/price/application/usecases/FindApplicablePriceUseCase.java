package com.inditex.price.application.usecases;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inditex.price.application.dto.PriceQueryRequestDTO;
import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.application.exceptions.PriceNotFoundException;
import com.inditex.price.application.mapper.PriceMapperDTO;
import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.domain.service.PriceDomainService;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Caso de uso para consultar precios aplicables
 * Orquesta la lógica de aplicación sin contener lógica de negocio
 */
@Service
public class FindApplicablePriceUseCase {

    private static final Logger logger = LogManager.getLogger(FindApplicablePriceUseCase.class);

    private final PriceRepository priceRepository;
    private final PriceDomainService priceDomainService;

    public FindApplicablePriceUseCase(PriceRepository priceRepository,
            PriceDomainService priceDomainService) {
        this.priceRepository = priceRepository;
        this.priceDomainService = priceDomainService;
    }

    /**
     * Ejecuta la consulta de precio aplicable en la fecha indicada
     * 
     * @param request parámetros de consulta
     * @return precio aplicable con mayor prioridad
     * @throws PriceNotFoundException si no se encuentra un precio aplicable
     */
    public PriceQueryResponseDTO execute(PriceQueryRequestDTO request) {

        BrandId brandId = new BrandId(request.getBrandId());
        ProductId productId = new ProductId(request.getProductId());

        // Buscar precios aplicables
        logger.debug("Buscando precios aplicables en repositorio...");
        List<Price> applicablePrices = priceRepository.findApplicablePrices(
                productId, brandId, request.getApplicationDate());

        // Manejar caso de repositorio que devuelve null
        if (applicablePrices == null) {
            logger.warn("El repositorio devolvió null para producto {} marca {} en fecha {}",
                    request.getProductId(), request.getBrandId(), request.getApplicationDate());
            applicablePrices = java.util.Collections.emptyList();
        }

        logger.info("Encontrados {} precios aplicables para producto {} marca {} en fecha {}",
                applicablePrices.size(), request.getProductId(), request.getBrandId(), request.getApplicationDate());

        // Seleccionar el precio con mayor prioridad
        Optional<Price> selectedPrice = priceDomainService.selectHighestPriorityPrice(applicablePrices);

        // Verificar que se encontró un precio
        Price price = selectedPrice.orElseThrow(() -> {
            logger.warn("No se encontró precio aplicable para producto {} de marca {} en fecha {}",
                    request.getProductId(), request.getBrandId(), request.getApplicationDate());

            return new PriceNotFoundException("No se encontró precio aplicable para producto "
                    + request.getProductId() + " de marca " + request.getBrandId()
                    + " en fecha " + request.getApplicationDate());
        });

        logger.info("Precio seleccionado - ID: {}, Precio: {}, Lista: {}, Prioridad: {}, ProductId: {}, BrandId: {}",
                price.getId(), price.getPrice().getAmount(), price.getPriceList(), price.getPriority(),
                request.getProductId(), request.getBrandId());

        // Convertir a DTO de respuesta
        PriceQueryResponseDTO response = PriceMapperDTO.INSTANCE.toResponseDTO(price);
        logger.debug("DTO de respuesta creado: {}", response);

        return response;
    }
}