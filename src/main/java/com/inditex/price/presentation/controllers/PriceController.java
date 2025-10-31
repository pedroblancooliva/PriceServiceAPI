package com.inditex.price.presentation.controllers;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inditex.price.application.dto.PriceQueryRequestDTO;
import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.application.exceptions.PriceNotFoundException;
import com.inditex.price.application.usecases.FindApplicablePriceUseCase;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Controlador REST para consultas de precios
 * Expone el endpoint de consulta de precios aplicables
 */
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {
    
    private final FindApplicablePriceUseCase findApplicablePriceUseCase;
    
    public PriceController(FindApplicablePriceUseCase findApplicablePriceUseCase) {
        this.findApplicablePriceUseCase = findApplicablePriceUseCase;
    }
    
    /**
     * Consulta el precio aplicable para un producto de una marca en una fecha específica
     * 
     * @param applicationDate fecha de aplicación del precio (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @param productId identificador del producto
     * @param brandId identificador de la marca/cadena
     * @return precio aplicable con mayor prioridad
     */
    @GetMapping
    public ResponseEntity<PriceQueryResponseDTO> getApplicablePrice(
            @RequestParam("applicationDate") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @NotNull(message = "La fecha de aplicación es obligatoria") 
            LocalDateTime applicationDate,
            
            @RequestParam("productId")
            @NotNull(message = "El ID del producto es obligatorio")
            @Positive(message = "El ID del producto debe ser positivo")
            Long productId,
            
            @RequestParam("brandId")
            @NotNull(message = "El ID de la marca es obligatorio")
            @Positive(message = "El ID de la marca debe ser positivo")
            Long brandId) {
        
        PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, productId, brandId);
        PriceQueryResponseDTO response = findApplicablePriceUseCase.execute(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Manejo de excepción cuando no se encuentra un precio aplicable
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePriceNotFound(PriceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Precio no encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    /**
     * Manejo de errores de validación
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * DTO para respuestas de error
     */
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        
        public ErrorResponse(int status, String error, String message) {
            this.status = status;
            this.error = error;
            this.message = message;
        }
        
        // Getters
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
    }
}