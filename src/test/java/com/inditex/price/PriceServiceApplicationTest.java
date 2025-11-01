package com.inditex.price;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

/**
 * Tests unitarios para PriceServiceApplication
 * Verifica que la aplicación Spring Boot se inicie correctamente
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PriceServiceApplicationTest {

    @Test
    @DisplayName("Debería cargar el contexto de Spring correctamente")
    void shouldLoadSpringContext(ApplicationContext context) {
        // Then
        assertNotNull(context);
        assertTrue(context.containsBean("priceController"));
        assertTrue(context.containsBean("findApplicablePriceUseCase"));
        assertTrue(context.containsBean("priceDomainService"));
    }

    @Test
    @DisplayName("Debería ejecutar main sin lanzar excepciones")
    void shouldRunMainWithoutExceptions() {
        // Given
        String[] args = {"--spring.main.web-environment=false", "--spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration"};
        
        // When & Then
        assertDoesNotThrow(() -> {
            // Simulamos el arranque sin el servidor web para evitar conflictos de puerto
            PriceServiceApplication.main(args);
        });
    }

    @Test
    @DisplayName("Debería tener anotación SpringBootApplication")
    void shouldHaveSpringBootApplicationAnnotation() {
        // Given
        Class<PriceServiceApplication> applicationClass = PriceServiceApplication.class;
        
        // When
        boolean hasAnnotation = applicationClass.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class);
        
        // Then
        assertTrue(hasAnnotation, "La clase debe tener la anotación @SpringBootApplication");
    }

    @Test
    @DisplayName("Debería ser una clase pública")
    void shouldBePublicClass() {
        // Given
        Class<PriceServiceApplication> applicationClass = PriceServiceApplication.class;
        
        // When
        int modifiers = applicationClass.getModifiers();
        
        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers), "La clase debe ser pública");
    }

    @Test
    @DisplayName("Debería tener método main estático")
    void shouldHaveStaticMainMethod() {
        // When & Then
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method mainMethod = PriceServiceApplication.class.getMethod("main", String[].class);
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()), "El método main debe ser estático");
            assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()), "El método main debe ser público");
        });
    }
}