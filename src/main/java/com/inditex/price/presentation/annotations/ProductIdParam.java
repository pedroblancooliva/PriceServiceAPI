package com.inditex.price.presentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Anotación compuesta para parámetros de ID de producto
 * Combina todas las validaciones y documentación necesarias
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "El ID del producto es obligatorio")
@Positive(message = "El ID del producto debe ser positivo")
@Parameter(description = "Identificador único del producto", 
           example = "35455", 
           required = true)
public @interface ProductIdParam {
}