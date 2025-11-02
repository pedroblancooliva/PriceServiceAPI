package com.inditex.price.presentation.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.inditex.price.application.exceptions.PriceNotFoundException;

/**
 * Manejador global de excepciones para la capa de presentación
 * Centraliza el manejo de errores siguiendo principios de arquitectura
 * hexagonal
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Manejo de excepción cuando no se encuentra un precio aplicable
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePriceNotFound(PriceNotFoundException ex) {
        logger.warn("Precio no encontrado: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Precio no encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Manejo de parámetros faltantes en la request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        logger.warn("Parámetro faltante en request: {} - tipo: {}", ex.getParameterName(), ex.getParameterType());

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Parámetro faltante",
                "El parámetro '" + ex.getParameterName() + "' es obligatorio");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Manejo de errores de conversión de tipos en parámetros
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.warn("Error de conversión de tipo - Parámetro: {}, Valor: {}, Tipo esperado: {}",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Formato de parámetro inválido",
                "El parámetro '" + ex.getName() + "' tiene un formato inválido");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Manejo de errores de validación
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException ex) {
        logger.warn("Error de validación: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error de validación", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Manejo de errores genéricos no capturados
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        logger.error("Error interno no controlado: {} - Clase: {}", ex.getMessage(), ex.getClass().getSimpleName(), ex);

        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor",
                "Ha ocurrido un error inesperado");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}