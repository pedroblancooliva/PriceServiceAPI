package com.inditex.price.application.usecases;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.inditex.price.infrastructure.persitence.mappers.PriceEntityMapper;

/**
 * Caso de uso para consultar precios aplicables
 * Orquesta la lógica de aplicación sin contener lógica de negocio
 */
@Service
public class FindApplicablePriceUseCase {
    
    private final PriceRepository priceRepository;
    private final PriceDomainService priceDomainService;
    private final PriceEntityMapper priceMapper;
    
    public FindApplicablePriceUseCase(PriceRepository priceRepository, 
                                     PriceDomainService priceDomainService,
                                     PriceEntityMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceDomainService = priceDomainService;
        this.priceMapper = priceMapper;
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
        List<Price> applicablePrices = priceRepository.findApplicablePrices(
                 productId, brandId,request.getApplicationDate()
        );
        
        // Aplicar regla de negocio para seleccionar el precio con mayor prioridad
        Optional<Price> selectedPrice = priceDomainService.selectHighestPriorityPrice(applicablePrices);
        
        // Verificar que se encontró un precio
        Price price = selectedPrice.orElseThrow(() -> 
                new PriceNotFoundException("No se encontró precio aplicable para producto " 
                        + request.getProductId() + " de marca " + request.getBrandId() 
                        + " en fecha " + request.getApplicationDate())
        );
        
        // Convertir a DTO de respuesta
        return PriceMapperDTO.INSTANCE.toResponseDTO(price);
    }
}