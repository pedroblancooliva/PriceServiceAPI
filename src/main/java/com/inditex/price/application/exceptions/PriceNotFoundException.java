package com.inditex.price.application.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un precio aplicable
 */
public class PriceNotFoundException extends RuntimeException {
    
    public PriceNotFoundException(String message) {
        super(message);
    }
    
    public PriceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}