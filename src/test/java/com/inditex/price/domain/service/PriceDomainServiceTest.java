package com.inditex.price.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Tests para PriceDomainService
 */
class PriceDomainServiceTest {

    private PriceDomainService priceDomainService;

    @BeforeEach
    void setUp() {
        priceDomainService = new PriceDomainService();
    }

    @Test
    void shouldReturnEmptyWhenListIsEmpty() {
        // Given
        List<Price> emptyPrices = Collections.emptyList();

        // When
        Optional<Price> result = priceDomainService.selectHighestPriorityPrice(emptyPrices);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnSinglePriceWhenListHasOneElement() {
        // Given
        Price price = createPrice(1L, 0);
        List<Price> prices = Arrays.asList(price);

        // When
        Optional<Price> result = priceDomainService.selectHighestPriorityPrice(prices);

        // Then
        assertTrue(result.isPresent());
        assertEquals(price, result.get());
    }

    @Test
    void shouldReturnHighestPriorityPrice() {
        // Given
        Price lowPriority = createPrice(1L, 0);
        Price mediumPriority = createPrice(2L, 1);
        Price highPriority = createPrice(3L, 2);
        
        List<Price> prices = Arrays.asList(lowPriority, mediumPriority, highPriority);

        // When
        Optional<Price> result = priceDomainService.selectHighestPriorityPrice(prices);

        // Then
        assertTrue(result.isPresent());
        assertEquals(highPriority, result.get());
    }

    @Test
    void shouldReturnLastPriceWhenMultiplePricesHaveSamePriority() {
        // Given
        Price price1 = createPrice(1L, 1);
        Price price2 = createPrice(2L, 1);
        Price price3 = createPrice(3L, 1);
        
        List<Price> prices = Arrays.asList(price1, price2, price3);

        // When
        Optional<Price> result = priceDomainService.selectHighestPriorityPrice(prices);

        // Then
        assertTrue(result.isPresent());
        assertEquals(price3, result.get()); // reduce devuelve el último elemento cuando las prioridades son iguales
    }

    private Price createPrice(Long id, Integer priorityValue) {
        return Price.builder()
                .id(id)
                .brandId(new BrandId(1L))
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .priceList(1)
                .productId(new ProductId(35455L))
                .priority(new Priority(priorityValue))
                .price(new Money(BigDecimal.valueOf(35.50), "EUR"))
                .build();
    }
}