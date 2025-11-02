package com.inditex.price.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para PriceNotFoundException
 * Cubre todos los constructores y métodos heredados
 */
class PriceNotFoundExceptionTest {

    @Test
    @DisplayName("Debería crear excepción con mensaje")
    void shouldCreateExceptionWithMessage() {
        // Given
        String expectedMessage = "No se encontró precio aplicable";

        // When
        PriceNotFoundException exception = new PriceNotFoundException(expectedMessage);

        // Then
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debería crear excepción con mensaje y causa")
    void shouldCreateExceptionWithMessageAndCause() {
        // Given
        String expectedMessage = "Error al buscar precio";
        Throwable expectedCause = new IllegalArgumentException("Parámetro inválido");

        // When
        PriceNotFoundException exception = new PriceNotFoundException(expectedMessage, expectedCause);

        // Then
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertSame(expectedCause, exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Debería crear excepción con mensaje null")
    void shouldCreateExceptionWithNullMessage() {
        // Given
        String nullMessage = null;

        // When
        PriceNotFoundException exception = new PriceNotFoundException(nullMessage);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Debería crear excepción con causa null")
    void shouldCreateExceptionWithNullCause() {
        // Given
        String message = "Test message";
        Throwable nullCause = null;

        // When
        PriceNotFoundException exception = new PriceNotFoundException(message, nullCause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Debería mantener la jerarquía de excepciones")
    void shouldMaintainExceptionHierarchy() {
        // Given
        PriceNotFoundException exception = new PriceNotFoundException("test");

        // When & Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("Debería ser lanzable y capturable")
    void shouldBeThrowableAndCatchable() {
        // Given
        String expectedMessage = "Price not found for testing";

        // When & Then
        try {
            throw new PriceNotFoundException(expectedMessage);
        } catch (PriceNotFoundException e) {
            assertEquals(expectedMessage, e.getMessage());
        } catch (Exception e) {
            // No debería llegar aquí
            assertTrue(false, "Should catch PriceNotFoundException specifically");
        }
    }

    @Test
    @DisplayName("Debería crear mensajes descriptivos para casos de negocio")
    void shouldCreateDescriptiveMessagesForBusinessCases() {
        // Test casos típicos de uso
        String productNotFound = "No se encontró precio para producto 35455 de marca 1 en fecha 2020-06-14T10:00:00";
        String noValidPrice = "No hay precio válido en el rango de fechas especificado";
        String invalidBrand = "Marca no válida para el producto especificado";

        PriceNotFoundException ex1 = new PriceNotFoundException(productNotFound);
        PriceNotFoundException ex2 = new PriceNotFoundException(noValidPrice);
        PriceNotFoundException ex3 = new PriceNotFoundException(invalidBrand);

        assertEquals(productNotFound, ex1.getMessage());
        assertEquals(noValidPrice, ex2.getMessage());
        assertEquals(invalidBrand, ex3.getMessage());
    }
}