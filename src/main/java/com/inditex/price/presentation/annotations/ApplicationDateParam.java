package com.inditex.price.presentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.constraints.NotNull;

/**
 * Anotación compuesta para parámetros de fecha de aplicación
 * Combina todas las validaciones y documentación necesarias
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "La fecha de aplicación es obligatoria")
@Parameter(description = "Fecha de aplicación del precio (formato: yyyy-MM-ddTHH:mm:ss)", 
           example = "2020-06-14T10:00:00", 
           required = true)
public @interface ApplicationDateParam {
}