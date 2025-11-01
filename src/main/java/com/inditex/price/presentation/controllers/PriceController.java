package com.inditex.price.presentation.controllers;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inditex.price.application.dto.PriceQueryRequestDTO;
import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.application.usecases.FindApplicablePriceUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Controlador REST para consultas de precios
 * Expone el endpoint de consulta de precios aplicables
 */
@RestController
@RequestMapping("/api/v1/prices")
@Tag(name = "Price Service", description = "API para consultar precios aplicables de productos")
public class PriceController {

	private static final Logger logger = LogManager.getLogger(PriceController.class);

	private final FindApplicablePriceUseCase findApplicablePriceUseCase;

	public PriceController(FindApplicablePriceUseCase findApplicablePriceUseCase) {
		this.findApplicablePriceUseCase = findApplicablePriceUseCase;
	}

	/**
	 * Consulta el precio aplicable para un producto de una marca en una fecha específica
	 * 
	 * @param applicationDate fecha de aplicación del precio
	 * @param productId       identificador del producto  
	 * @param brandId         identificador de la marca/cadena
	 * @return precio aplicable con mayor prioridad
	 */
	@GetMapping
	@Operation(summary = "Consultar precio aplicable", 
			   description = "Obtiene el precio aplicable para un producto de una marca en una fecha específica")
	public ResponseEntity<PriceQueryResponseDTO> getApplicablePrice(
			@RequestParam("applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
			@NotNull(message = "La fecha de aplicación es obligatoria") LocalDateTime applicationDate,

			@RequestParam("productId") @NotNull(message = "El ID del producto es obligatorio") 
			@Positive(message = "El ID del producto debe ser positivo") Long productId,

			@RequestParam("brandId") @NotNull(message = "El ID de la marca es obligatorio") 
			@Positive(message = "El ID de la marca debe ser positivo") Long brandId) {

		long startTime = System.currentTimeMillis();
		
		logger.info("Iniciando consulta de precio - productId: {}, brandId: {}, fecha: {}", 
				   productId, brandId, applicationDate);

		try {
			PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, productId, brandId);
			logger.debug("Request DTO creado: {}", request);
			
			PriceQueryResponseDTO response = findApplicablePriceUseCase.execute(request);
			
			long duration = System.currentTimeMillis() - startTime;
			
			logger.info("Consulta completada exitosamente - productId: {}, brandId: {}, precio: {}, lista: {}, duración: {}ms", 
					   productId, brandId, response.getPrice(), response.getPriceList(), duration);

			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			long duration = System.currentTimeMillis() - startTime;
			
			logger.error("Error en consulta de precio - productId: {}, brandId: {}, fecha: {}, duración: {}ms, error: {}", 
						productId, brandId, applicationDate, duration, e.getMessage(), e);
			
			throw e; // Re-lanzar para que el GlobalExceptionHandler la maneje
		}
	}
}