package com.inditex.price.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Tests de integración para el repositorio de precios
 * Valida el comportamiento de la persistencia con datos reales
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // opcional si usas tu BD real
class PriceRepositoryIntegrationTest {

    @Autowired
    private PriceRepository priceRepository;

    @Test
    @DisplayName("Debería encontrar precios aplicables para producto y marca válidos")
    void shouldFindApplicablePricesForValidProductAndBrand() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId brandId = new BrandId(1L);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        // When
        List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, applicationDate);

        // Then
        assertNotNull(prices);
        assertFalse(prices.isEmpty());
        assertEquals(1, prices.size());
        
        Price price = prices.get(0);
        assertEquals(productId, price.getProductId());
        assertEquals(brandId, price.getBrandId());
        assertTrue(price.isApplicableAt(applicationDate));
    }

    @Test
    @DisplayName("Debería encontrar múltiples precios cuando hay solapamiento")
    void shouldFindMultiplePricesWhenOverlapping() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId brandId = new BrandId(1L);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0); // Durante promoción

        // When
        List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, applicationDate);

        // Then
        assertNotNull(prices);
        assertEquals(2, prices.size()); // Precio base + promocional
        
        // Verificar que ambos precios son aplicables en esa fecha
        prices.forEach(price -> {
            assertEquals(productId, price.getProductId());
            assertEquals(brandId, price.getBrandId());
            assertTrue(price.isApplicableAt(applicationDate));
        });
    }

    @Test
    @DisplayName("Debería devolver lista vacía para producto inexistente")
    void shouldReturnEmptyListForNonExistentProduct() {
        // Given
        ProductId nonExistentProductId = new ProductId(99999L);
        BrandId brandId = new BrandId(1L);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        // When
        List<Price> prices = priceRepository.findApplicablePrices(nonExistentProductId, brandId, applicationDate);

        // Then
        assertNotNull(prices);
        assertTrue(prices.isEmpty());
    }

    @Test
    @DisplayName("Debería devolver lista vacía para marca inexistente")
    void shouldReturnEmptyListForNonExistentBrand() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId nonExistentBrandId = new BrandId(999L);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        // When
        List<Price> prices = priceRepository.findApplicablePrices(productId, nonExistentBrandId, applicationDate);

        // Then
        assertNotNull(prices);
        assertTrue(prices.isEmpty());
    }

    @Test
    @DisplayName("Debería devolver lista vacía para fecha fuera de rango")
    void shouldReturnEmptyListForDateOutOfRange() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId brandId = new BrandId(1L);
        LocalDateTime dateOutOfRange = LocalDateTime.of(2014, 6, 14, 10, 0); // Año anterior

        // When
        List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, dateOutOfRange);

        // Then
        assertNotNull(prices);
        assertTrue(prices.isEmpty());
    }

    @Test
    @DisplayName("Debería encontrar el precio correcto en límites exactos")
    void shouldFindCorrectPriceAtExactBoundaries() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId brandId = new BrandId(1L);

        // Inicio exacto de la promoción
        LocalDateTime promotionStart = LocalDateTime.of(2020, 6, 14, 15, 0);
        List<Price> pricesAtStart = priceRepository.findApplicablePrices(productId, brandId, promotionStart);
        
        // Final exacto de la promoción
        LocalDateTime promotionEnd = LocalDateTime.of(2020, 6, 14, 18, 30);
        List<Price> pricesAtEnd = priceRepository.findApplicablePrices(productId, brandId, promotionEnd);

        // Then
        assertNotNull(pricesAtStart);
        assertNotNull(pricesAtEnd);
        assertEquals(2, pricesAtStart.size()); // Base + promocional
        assertEquals(2, pricesAtEnd.size());   // Base + promocional
    }

    @Test
    @DisplayName("Debería manejar consultas con diferentes formatos de fecha")
    void shouldHandleDifferentDateFormats() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId brandId = new BrandId(1L);
        
        LocalDateTime[] testDates = {
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),     // Medianoche
                LocalDateTime.of(2020, 6, 14, 12, 30, 45),  // Con segundos
                LocalDateTime.of(2020, 6, 14, 23, 59, 59)   // Final del día
        };

        // When & Then
        for (LocalDateTime date : testDates) {
            List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, date);
            assertNotNull(prices);
            assertFalse(prices.isEmpty(), "No se encontraron precios para la fecha: " + date);
        }
    }

    @Test
    @DisplayName("Test de rendimiento: Múltiples consultas concurrentes")
    void shouldHandleMultipleConcurrentQueries() {
        // Given
        ProductId productId = new ProductId(35455L);
        BrandId brandId = new BrandId(1L);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        
        // When - Simular múltiples consultas
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, applicationDate);
            assertNotNull(prices);
            assertFalse(prices.isEmpty());
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Then - Las consultas deberían ser rápidas (menos de 1 segundo para 100 consultas)
        assertTrue(duration < 1000, "Las consultas tardaron más de lo esperado: " + duration + "ms");
    }
}