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
import com.inditex.price.presentation.annotations.ApplicationDateParam;
import com.inditex.price.presentation.annotations.BrandIdParam;
import com.inditex.price.presentation.annotations.ProductIdParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	 * Consulta el precio aplicable para un producto de una marca en una fecha
	 * específica
	 * 
	 * @param applicationDate fecha de aplicación del precio
	 * @param productId       identificador del producto
	 * @param brandId         identificador de la marca/cadena
	 * @return precio aplicable con mayor prioridad
	 */
	@GetMapping
	@Operation(summary = "Consultar precio aplicable", description = "Obtiene el precio aplicable para un producto de una marca en una fecha específica")
	public ResponseEntity<PriceQueryResponseDTO> getApplicablePrice(
			@RequestParam("applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ApplicationDateParam LocalDateTime applicationDate,

			@RequestParam("productId") @ProductIdParam Long productId,

			@RequestParam("brandId") @BrandIdParam Long brandId) {

		long startTime = System.currentTimeMillis();

		logger.info("Iniciando consulta de precio - productId: {}, brandId: {}, fecha: {}",
				productId, brandId, applicationDate);

		try {
			PriceQueryRequestDTO request = createPriceQueryRequest(applicationDate, productId, brandId);

			PriceQueryResponseDTO response = findApplicablePriceUseCase.execute(request);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error en consulta de precio - productId: {}, brandId: {}, fecha: {}, error: {}",
					productId, brandId, applicationDate, e.getMessage(), e);
			throw e; // Re-lanzar para que el GlobalExceptionHandler la maneje
		}
	}

	/**
	 * Método auxiliar para crear el DTO de request
	 * w
	 */
	private PriceQueryRequestDTO createPriceQueryRequest(LocalDateTime applicationDate, Long productId, Long brandId) {
		return new PriceQueryRequestDTO(applicationDate, productId, brandId);
	}

}