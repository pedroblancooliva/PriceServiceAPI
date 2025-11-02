package com.inditex.price.infrastructure.persitence.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;
import com.inditex.price.infrastructure.persitence.entity.PriceJpaEntity;

/**
 * Tests unitarios para PriceEntityMapper
 * Cubre la conversión bidireccional entre entidades JPA y entidades de dominio
 */
class PriceEntityMapperTest {

    private PriceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceEntityMapper();
    }

    @Test
    @DisplayName("Debería convertir entidad JPA a dominio correctamente")
    void shouldConvertJpaEntityToDomain() {
        // Given
        PriceJpaEntity jpaEntity = new PriceJpaEntity(
                1L, // brandId
                LocalDateTime.of(2020, 6, 14, 0, 0), // startDate
                LocalDateTime.of(2020, 12, 31, 23, 59), // endDate
                1, // priceList
                35455L, // productId
                0, // priority
                new BigDecimal("35.50"), // price
                "EUR" // currency
        );
        jpaEntity.setId(1L);

        // When
        Price domainPrice = mapper.toDomain(jpaEntity);

        // Then
        assertNotNull(domainPrice);
        assertEquals(Long.valueOf(1L), domainPrice.getId());
        assertEquals(Long.valueOf(1L), domainPrice.getBrandId().getValue());
        assertEquals(LocalDateTime.of(2020, 6, 14, 0, 0), domainPrice.getStartDate());
        assertEquals(LocalDateTime.of(2020, 12, 31, 23, 59), domainPrice.getEndDate());
        assertEquals(Integer.valueOf(1), domainPrice.getPriceList());
        assertEquals(Long.valueOf(35455L), domainPrice.getProductId().getValue());
        assertEquals(Integer.valueOf(0), domainPrice.getPriority().getValue());
        assertEquals(new BigDecimal("35.50"), domainPrice.getPrice().getAmount());
        assertEquals("EUR", domainPrice.getPrice().getCurrency());
    }

    @Test
    @DisplayName("Debería convertir entidad de dominio a JPA correctamente")
    void shouldConvertDomainToJpaEntity() {
        // Given
        Price domainPrice = Price.builder()
                .id(1L)
                .brandId(new BrandId(1L))
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .priceList(1)
                .productId(new ProductId(35455L))
                .priority(new Priority(0))
                .price(new Money(new BigDecimal("35.50"), "EUR"))
                .build();

        // When
        PriceJpaEntity jpaEntity = mapper.toEntity(domainPrice);

        // Then
        assertNotNull(jpaEntity);
        assertEquals(Long.valueOf(1L), jpaEntity.getId());
        assertEquals(Long.valueOf(1L), jpaEntity.getBrandId());
        assertEquals(LocalDateTime.of(2020, 6, 14, 0, 0), jpaEntity.getStartDate());
        assertEquals(LocalDateTime.of(2020, 12, 31, 23, 59), jpaEntity.getEndDate());
        assertEquals(Integer.valueOf(1), jpaEntity.getPriceList());
        assertEquals(Long.valueOf(35455L), jpaEntity.getProductId());
        assertEquals(Integer.valueOf(0), jpaEntity.getPriority());
        assertEquals(new BigDecimal("35.50"), jpaEntity.getPrice());
        assertEquals("EUR", jpaEntity.getCurrency());
    }

    @Test
    @DisplayName("Debería manejar entidad JPA null")
    void shouldHandleNullJpaEntity() {
        // Given
        PriceJpaEntity nullEntity = null;

        // When
        Price result = mapper.toDomain(nullEntity);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Debería manejar entidad de dominio null")
    void shouldHandleNullDomainEntity() {
        // Given
        Price nullDomain = null;

        // When
        PriceJpaEntity result = mapper.toEntity(nullDomain);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Debería convertir entidad de dominio sin ID")
    void shouldConvertDomainEntityWithoutId() {
        // Given
        Price domainPrice = Price.builder()
                .brandId(new BrandId(2L))
                .startDate(LocalDateTime.of(2020, 6, 15, 10, 0))
                .endDate(LocalDateTime.of(2020, 6, 15, 18, 30))
                .priceList(2)
                .productId(new ProductId(35455L))
                .priority(new Priority(1))
                .price(new Money(new BigDecimal("25.45"), "EUR"))
                .build();

        // When
        PriceJpaEntity jpaEntity = mapper.toEntity(domainPrice);

        // Then
        assertNotNull(jpaEntity);
        assertNull(jpaEntity.getId()); // ID debe ser null para nuevas entidades
        assertEquals(Long.valueOf(2L), jpaEntity.getBrandId());
        assertEquals(LocalDateTime.of(2020, 6, 15, 10, 0), jpaEntity.getStartDate());
        assertEquals(LocalDateTime.of(2020, 6, 15, 18, 30), jpaEntity.getEndDate());
        assertEquals(Integer.valueOf(2), jpaEntity.getPriceList());
        assertEquals(Long.valueOf(35455L), jpaEntity.getProductId());
        assertEquals(Integer.valueOf(1), jpaEntity.getPriority());
        assertEquals(new BigDecimal("25.45"), jpaEntity.getPrice());
        assertEquals("EUR", jpaEntity.getCurrency());
    }

    @Test
    @DisplayName("Debería realizar conversión bidireccional correctamente")
    void shouldPerformBidirectionalConversionCorrectly() {
        // Given - Entidad JPA original
        PriceJpaEntity originalJpa = new PriceJpaEntity(
                3L, // brandId
                LocalDateTime.of(2020, 6, 16, 0, 0), // startDate
                LocalDateTime.of(2020, 6, 16, 23, 59), // endDate
                4, // priceList
                35455L, // productId
                1, // priority
                new BigDecimal("38.95"), // price
                "EUR" // currency
        );
        originalJpa.setId(4L);

        // When - JPA -> Domain -> JPA
        Price domainPrice = mapper.toDomain(originalJpa);
        PriceJpaEntity convertedJpa = mapper.toEntity(domainPrice);

        // Then - Debe mantener todos los valores
        assertNotNull(convertedJpa);
        assertEquals(originalJpa.getId(), convertedJpa.getId());
        assertEquals(originalJpa.getBrandId(), convertedJpa.getBrandId());
        assertEquals(originalJpa.getStartDate(), convertedJpa.getStartDate());
        assertEquals(originalJpa.getEndDate(), convertedJpa.getEndDate());
        assertEquals(originalJpa.getPriceList(), convertedJpa.getPriceList());
        assertEquals(originalJpa.getProductId(), convertedJpa.getProductId());
        assertEquals(originalJpa.getPriority(), convertedJpa.getPriority());
        assertEquals(originalJpa.getPrice(), convertedJpa.getPrice());
        assertEquals(originalJpa.getCurrency(), convertedJpa.getCurrency());
    }

    @Test
    @DisplayName("Debería manejar diferentes tipos de moneda")
    void shouldHandleDifferentCurrencies() {
        // Given
        PriceJpaEntity usdEntity = new PriceJpaEntity(
                1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                1, 12345L, 0, new BigDecimal("42.75"), "USD");

        PriceJpaEntity gbpEntity = new PriceJpaEntity(
                2L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                2, 67890L, 1, new BigDecimal("28.99"), "GBP");

        // When
        Price usdDomain = mapper.toDomain(usdEntity);
        Price gbpDomain = mapper.toDomain(gbpEntity);

        // Then
        assertEquals("USD", usdDomain.getPrice().getCurrency());
        assertEquals("GBP", gbpDomain.getPrice().getCurrency());
        assertEquals(new BigDecimal("42.75"), usdDomain.getPrice().getAmount());
        assertEquals(new BigDecimal("28.99"), gbpDomain.getPrice().getAmount());
    }

    @Test
    @DisplayName("Debería manejar valores extremos de priority")
    void shouldHandleExtremePriorityValues() {
        // Given
        PriceJpaEntity maxPriorityEntity = new PriceJpaEntity(
                1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                1, 35455L, Integer.MAX_VALUE, new BigDecimal("100.00"), "EUR");

        PriceJpaEntity minPriorityEntity = new PriceJpaEntity(
                1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                1, 35455L, 0, new BigDecimal("50.00"), "EUR");

        // When
        Price maxPriorityDomain = mapper.toDomain(maxPriorityEntity);
        Price minPriorityDomain = mapper.toDomain(minPriorityEntity);

        // Then
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), maxPriorityDomain.getPriority().getValue());
        assertEquals(Integer.valueOf(0), minPriorityDomain.getPriority().getValue());
    }
}