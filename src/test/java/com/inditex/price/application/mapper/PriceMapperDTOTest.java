package com.inditex.price.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Tests unitarios para PriceMapperDTO
 * Cubre la conversión de entidades de dominio a DTOs de respuesta
 * y métodos de mapeo de Value Objects
 */
class PriceMapperDTOTest {

    private PriceMapperDTOImpl mapper;

    @BeforeEach
    void setUp() {
        // Usamos la implementación directamente para cubrir todas las líneas generadas
        mapper = new PriceMapperDTOImpl();
    }

    @Test
    @DisplayName("Debería convertir Price a PriceQueryResponseDTO correctamente")
    void shouldConvertPriceToPriceQueryResponseDTO() {
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
        PriceQueryResponseDTO responseDTO = mapper.toResponseDTO(domainPrice);

        // Then
        assertNotNull(responseDTO);
        assertEquals(Long.valueOf(35455L), responseDTO.getProductId());
        assertEquals(Long.valueOf(1L), responseDTO.getBrandId());
        assertEquals(Integer.valueOf(1), responseDTO.getPriceList());
        assertEquals(new BigDecimal("35.50"), responseDTO.getPrice());
        assertEquals("EUR", responseDTO.getCurrency());
    }

    @Test
    @DisplayName("Debería manejar Price null")
    void shouldHandleNullPrice() {
        // Given
        Price nullPrice = null;

        // When
        PriceQueryResponseDTO result = mapper.toResponseDTO(nullPrice);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Debería manejar Price con ProductId null usando mock")
    void shouldHandlePriceWithNullProductId() {
        // Given
        Price mockPrice = mock(Price.class);
        when(mockPrice.getProductId()).thenReturn(null);
        when(mockPrice.getBrandId()).thenReturn(new BrandId(1L));
        when(mockPrice.getPrice()).thenReturn(new Money(new BigDecimal("25.45"), "EUR"));
        when(mockPrice.getPriceList()).thenReturn(1);

        // When
        PriceQueryResponseDTO result = mapper.toResponseDTO(mockPrice);

        // Then
        assertNotNull(result);
        assertNull(result.getProductId());
        assertEquals(Long.valueOf(1L), result.getBrandId());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
        assertEquals("EUR", result.getCurrency());
        assertEquals(Integer.valueOf(1), result.getPriceList());
    }

    @Test
    @DisplayName("Debería manejar Price con BrandId null usando mock")
    void shouldHandlePriceWithNullBrandId() {
        // Given
        Price mockPrice = mock(Price.class);
        when(mockPrice.getProductId()).thenReturn(new ProductId(35455L));
        when(mockPrice.getBrandId()).thenReturn(null);
        when(mockPrice.getPrice()).thenReturn(new Money(new BigDecimal("25.45"), "EUR"));
        when(mockPrice.getPriceList()).thenReturn(1);

        // When
        PriceQueryResponseDTO result = mapper.toResponseDTO(mockPrice);

        // Then
        assertNotNull(result);
        assertEquals(Long.valueOf(35455L), result.getProductId());
        assertNull(result.getBrandId());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
        assertEquals("EUR", result.getCurrency());
        assertEquals(Integer.valueOf(1), result.getPriceList());
    }

    @Test
    @DisplayName("Debería manejar Price con Money null usando mock")
    void shouldHandlePriceWithNullMoney() {
        // Given
        Price mockPrice = mock(Price.class);
        when(mockPrice.getProductId()).thenReturn(new ProductId(35455L));
        when(mockPrice.getBrandId()).thenReturn(new BrandId(1L));
        when(mockPrice.getPrice()).thenReturn(null);
        when(mockPrice.getPriceList()).thenReturn(1);

        // When
        PriceQueryResponseDTO result = mapper.toResponseDTO(mockPrice);

        // Then
        assertNotNull(result);
        assertEquals(Long.valueOf(35455L), result.getProductId());
        assertEquals(Long.valueOf(1L), result.getBrandId());
        assertNull(result.getPrice());
        assertNull(result.getCurrency());
        assertEquals(Integer.valueOf(1), result.getPriceList());
    }

    @Test
    @DisplayName("Debería mapear ProductId correctamente")
    void shouldMapProductIdCorrectly() {
        // Given
        ProductId productId = new ProductId(12345L);
        ProductId nullProductId = null;

        // When
        Long mappedValue = mapper.map(productId);
        Long mappedNullValue = mapper.map(nullProductId);

        // Then
        assertEquals(Long.valueOf(12345L), mappedValue);
        assertNull(mappedNullValue);
    }

    @Test
    @DisplayName("Debería mapear BrandId correctamente")
    void shouldMapBrandIdCorrectly() {
        // Given
        BrandId brandId = new BrandId(2L);
        BrandId nullBrandId = null;

        // When
        Long mappedValue = mapper.map(brandId);
        Long mappedNullValue = mapper.map(nullBrandId);

        // Then
        assertEquals(Long.valueOf(2L), mappedValue);
        assertNull(mappedNullValue);
    }

    @Test
    @DisplayName("Debería mapear Money amount correctamente")
    void shouldMapMoneyAmountCorrectly() {
        // Given
        Money money = new Money(new BigDecimal("99.99"), "USD");
        Money nullMoney = null;

        // When
        BigDecimal mappedAmount = mapper.map(money);
        BigDecimal mappedNullAmount = mapper.map(nullMoney);

        // Then
        assertEquals(new BigDecimal("99.99"), mappedAmount);
        assertNull(mappedNullAmount);
    }

    @Test
    @DisplayName("Debería mapear Money currency correctamente")
    void shouldMapMoneyCurrencyCorrectly() {
        // Given
        Money money = new Money(new BigDecimal("25.45"), "GBP");
        Money nullMoney = null;

        // When
        String mappedCurrency = mapper.mapCurrency(money);
        String mappedNullCurrency = mapper.mapCurrency(nullMoney);

        // Then
        assertEquals("GBP", mappedCurrency);
        assertNull(mappedNullCurrency);
    }

    @Test
    @DisplayName("Debería manejar diferentes monedas")
    void shouldHandleDifferentCurrencies() {
        // Given
        Price eurPrice = createPriceWithCurrency("EUR", new BigDecimal("35.50"));
        Price usdPrice = createPriceWithCurrency("USD", new BigDecimal("42.75"));
        Price gbpPrice = createPriceWithCurrency("GBP", new BigDecimal("28.99"));

        // When
        PriceQueryResponseDTO eurResponse = mapper.toResponseDTO(eurPrice);
        PriceQueryResponseDTO usdResponse = mapper.toResponseDTO(usdPrice);
        PriceQueryResponseDTO gbpResponse = mapper.toResponseDTO(gbpPrice);

        // Then
        assertEquals("EUR", eurResponse.getCurrency());
        assertEquals("USD", usdResponse.getCurrency());
        assertEquals("GBP", gbpResponse.getCurrency());
        assertEquals(new BigDecimal("35.50"), eurResponse.getPrice());
        assertEquals(new BigDecimal("42.75"), usdResponse.getPrice());
        assertEquals(new BigDecimal("28.99"), gbpResponse.getPrice());
    }

    @Test
    @DisplayName("Debería manejar diferentes IDs de producto y marca")
    void shouldHandleDifferentProductAndBrandIds() {
        // Given
        Price price1 = createPriceWithIds(11111L, 1L);
        Price price2 = createPriceWithIds(22222L, 2L);
        Price price3 = createPriceWithIds(99999L, 10L);

        // When
        PriceQueryResponseDTO response1 = mapper.toResponseDTO(price1);
        PriceQueryResponseDTO response2 = mapper.toResponseDTO(price2);
        PriceQueryResponseDTO response3 = mapper.toResponseDTO(price3);

        // Then
        assertEquals(Long.valueOf(11111L), response1.getProductId());
        assertEquals(Long.valueOf(1L), response1.getBrandId());
        assertEquals(Long.valueOf(22222L), response2.getProductId());
        assertEquals(Long.valueOf(2L), response2.getBrandId());
        assertEquals(Long.valueOf(99999L), response3.getProductId());
        assertEquals(Long.valueOf(10L), response3.getBrandId());
    }

    @Test
    @DisplayName("Debería mantener precisión decimal en precios")
    void shouldMaintainDecimalPrecisionInPrices() {
        // Given
        BigDecimal precisePrice = new BigDecimal("123.456789");
        Price price = createPriceWithAmount(precisePrice);

        // When
        PriceQueryResponseDTO response = mapper.toResponseDTO(price);

        // Then
        assertEquals(precisePrice, response.getPrice());
        assertEquals(0, precisePrice.compareTo(response.getPrice()));
    }

    @Test
    @DisplayName("Debería mapear correctamente con diferentes PriceList")
    void shouldMapCorrectlyWithDifferentPriceList() {
        // Given
        Price priceList1 = createPriceWithPriceList(1);
        Price priceList2 = createPriceWithPriceList(2);
        Price priceList99 = createPriceWithPriceList(99);

        // When
        PriceQueryResponseDTO response1 = mapper.toResponseDTO(priceList1);
        PriceQueryResponseDTO response2 = mapper.toResponseDTO(priceList2);
        PriceQueryResponseDTO response99 = mapper.toResponseDTO(priceList99);

        // Then
        assertEquals(Integer.valueOf(1), response1.getPriceList());
        assertEquals(Integer.valueOf(2), response2.getPriceList());
        assertEquals(Integer.valueOf(99), response99.getPriceList());
    }

    @Test
    @DisplayName("Debería verificar la cobertura de casos extremos con valores límite")
    void shouldVerifyEdgeCasesWithBoundaryValues() {
        // Given - Valores límite para testing de cobertura
        Price minPrice = createPriceWithAmount(BigDecimal.ZERO);
        Price maxPrice = createPriceWithAmount(new BigDecimal("999999.99"));
        
        // When
        PriceQueryResponseDTO minResult = mapper.toResponseDTO(minPrice);
        PriceQueryResponseDTO maxResult = mapper.toResponseDTO(maxPrice);

        // Then
        assertEquals(BigDecimal.ZERO, minResult.getPrice());
        assertEquals(new BigDecimal("999999.99"), maxResult.getPrice());
    }

    @Test
    @DisplayName("Debería manejar monedas con caracteres especiales")
    void shouldHandleCurrenciesWithSpecialCharacters() {
        // Given
        Price jpyPrice = createPriceWithCurrency("JPY", new BigDecimal("1000"));
        Price chfPrice = createPriceWithCurrency("CHF", new BigDecimal("50.25"));

        // When
        PriceQueryResponseDTO jpyResponse = mapper.toResponseDTO(jpyPrice);
        PriceQueryResponseDTO chfResponse = mapper.toResponseDTO(chfPrice);

        // Then
        assertEquals("JPY", jpyResponse.getCurrency());
        assertEquals("CHF", chfResponse.getCurrency());
        assertEquals(new BigDecimal("1000"), jpyResponse.getPrice());
        assertEquals(new BigDecimal("50.25"), chfResponse.getPrice());
    }

    @Test
    @DisplayName("Debería manejar IDs de producto y marca con valores extremos")
    void shouldHandleProductAndBrandIdsWithExtremeValues() {
        // Given
        Price minIdPrice = createPriceWithIds(1L, 1L);
        Price maxIdPrice = createPriceWithIds(Long.MAX_VALUE, Long.MAX_VALUE);

        // When
        PriceQueryResponseDTO minIdResponse = mapper.toResponseDTO(minIdPrice);
        PriceQueryResponseDTO maxIdResponse = mapper.toResponseDTO(maxIdPrice);

        // Then
        assertEquals(Long.valueOf(1L), minIdResponse.getProductId());
        assertEquals(Long.valueOf(1L), minIdResponse.getBrandId());
        assertEquals(Long.valueOf(Long.MAX_VALUE), maxIdResponse.getProductId());
        assertEquals(Long.valueOf(Long.MAX_VALUE), maxIdResponse.getBrandId());
    }

    @Test
    @DisplayName("Debería verificar la conversión completa con todos los campos")
    void shouldVerifyCompleteConversionWithAllFields() {
        // Given
        Price completePrice = Price.builder()
                .id(999L)
                .brandId(new BrandId(5L))
                .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .priceList(3)
                .productId(new ProductId(88888L))
                .priority(new Priority(5))
                .price(new Money(new BigDecimal("199.99"), "USD"))
                .build();

        // When
        PriceQueryResponseDTO result = mapper.toResponseDTO(completePrice);

        // Then
        assertNotNull(result);
        assertEquals(Long.valueOf(88888L), result.getProductId());
        assertEquals(Long.valueOf(5L), result.getBrandId());
        assertEquals(Integer.valueOf(3), result.getPriceList());
        assertEquals(new BigDecimal("199.99"), result.getPrice());
        assertEquals("USD", result.getCurrency());
    }

    // Métodos helper para crear objetos de test
    private Price createPriceWithCurrency(String currency, BigDecimal amount) {
        return Price.builder()
                .id(1L)
                .brandId(new BrandId(1L))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(1)
                .productId(new ProductId(35455L))
                .priority(new Priority(0))
                .price(new Money(amount, currency))
                .build();
    }

    private Price createPriceWithIds(Long productId, Long brandId) {
        return Price.builder()
                .id(1L)
                .brandId(new BrandId(brandId))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(1)
                .productId(new ProductId(productId))
                .priority(new Priority(0))
                .price(new Money(new BigDecimal("35.50"), "EUR"))
                .build();
    }

    private Price createPriceWithAmount(BigDecimal amount) {
        return Price.builder()
                .id(1L)
                .brandId(new BrandId(1L))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(1)
                .productId(new ProductId(35455L))
                .priority(new Priority(0))
                .price(new Money(amount, "EUR"))
                .build();
    }

    private Price createPriceWithPriceList(Integer priceList) {
        return Price.builder()
                .id(1L)
                .brandId(new BrandId(1L))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(priceList)
                .productId(new ProductId(35455L))
                .priority(new Priority(0))
                .price(new Money(new BigDecimal("35.50"), "EUR"))
                .build();
    }
}