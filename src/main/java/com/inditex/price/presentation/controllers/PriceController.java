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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
        summary = "Consultar precio aplicable",
        description = "Obtiene el precio aplicable para un producto de una marca en una fecha específica. " +
                     "Si existen múltiples tarifas aplicables, devuelve la de mayor prioridad.",
        tags = {"Price Service"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Precio encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PriceQueryResponseDTO.class),
                examples = @ExampleObject(
                    name = "Ejemplo de respuesta exitosa",
                    summary = "Precio aplicable encontrado",
                    value = "{\n" +
                           "  \"productId\": 35455,\n" +
                           "  \"brandId\": 1,\n" +
                           "  \"priceList\": 1,\n" +
                           "  \"startDate\": \"2020-06-14T00:00:00\",\n" +
                           "  \"endDate\": \"2020-12-31T23:59:59\",\n" +
                           "  \"price\": 35.50,\n" +
                           "  \"currency\": \"EUR\"\n" +
                           "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontró precio aplicable para los parámetros especificados",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Precio no encontrado",
                    summary = "Error cuando no existe precio aplicable",
                    value = "{\n" +
                           "  \"status\": 404,\n" +
                           "  \"error\": \"Precio no encontrado\",\n" +
                           "  \"message\": \"No se encontró precio aplicable para los parámetros especificados\"\n" +
                           "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Parámetros inválidos",
                    summary = "Error de validación de parámetros",
                    value = "{\n" +
                           "  \"status\": 400,\n" +
                           "  \"error\": \"Error de validación\",\n" +
                           "  \"message\": \"Los parámetros proporcionados no son válidos\"\n" +
                           "}"
                )
            )
        )
    })
    public ResponseEntity<PriceQueryResponseDTO> getApplicablePrice(
            @Parameter(
                description = "Fecha de aplicación del precio (formato: yyyy-MM-ddTHH:mm:ss)",
                example = "2020-06-14T10:00:00",
                required = true
            )
            @RequestParam("applicationDate") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @NotNull(message = "La fecha de aplicación es obligatoria") 
            LocalDateTime applicationDate,
            
            @Parameter(
                description = "Identificador único del producto",
                example = "35455",
                required = true
            )
            @RequestParam("productId")
            @NotNull(message = "El ID del producto es obligatorio")
            @Positive(message = "El ID del producto debe ser positivo")
            Long productId,
            
            @Parameter(
                description = "Identificador de la marca (cadena)",
                example = "1",
                required = true
            )
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