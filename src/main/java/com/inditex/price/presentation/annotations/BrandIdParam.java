package com.inditex.price.presentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Anotación compuesta para parámetros de ID de marca
 * Combina todas las validaciones y documentación necesarias
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "El ID de la marca es obligatorio")
@Positive(message = "El ID de la marca debe ser positivo")
@Parameter(description = "Identificador de la marca (cadena)", example = "1", required = true)
public @interface BrandIdParam {
}