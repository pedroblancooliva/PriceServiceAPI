package com.inditex.price.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inditex.price.application.dto.PriceQueryRequestDTO;
import com.inditex.price.application.dto.PriceQueryResponseDTO;
import com.inditex.price.application.exceptions.PriceNotFoundException;
import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.domain.service.PriceDomainService;
import com.inditex.price.domain.valueobject.BrandId;
import com.inditex.price.domain.valueobject.Money;
import com.inditex.price.domain.valueobject.Priority;
import com.inditex.price.domain.valueobject.ProductId;

/**
 * Tests unitarios completos para FindApplicablePriceUseCase
 * Cubre todos los escenarios posibles incluidos casos edge
 */
@ExtendWith(MockitoExtension.class)
class FindApplicablePriceUseCaseTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private PriceDomainService priceDomainService;

    private FindApplicablePriceUseCase useCase;
    
    private Validator validator;

    @BeforeEach
    void setUp() {
        useCase = new FindApplicablePriceUseCase(priceRepository, priceDomainService);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debería ejecutar exitosamente cuando encuentra un precio")
    void shouldExecuteSuccessfullyWhenPriceFound() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, 35455L, 1L);
        Price mockPrice = createMockPrice(1L, 0, BigDecimal.valueOf(35.50));

        when(priceRepository.findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(applicationDate)))
                .thenReturn(Arrays.asList(mockPrice));
        when(priceDomainService.selectHighestPriorityPrice(Arrays.asList(mockPrice)))
                .thenReturn(Optional.of(mockPrice));

        // When
        PriceQueryResponseDTO result = useCase.execute(request);

        // Then
        assertNotNull(result);
        assertEquals(Long.valueOf(35455L), result.getProductId());
        assertEquals(Long.valueOf(1L), result.getBrandId());
        assertEquals(Integer.valueOf(1), result.getPriceList());
        assertEquals(BigDecimal.valueOf(35.50), result.getPrice());
        assertEquals("EUR", result.getCurrency());

        verify(priceRepository).findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(applicationDate));
        verify(priceDomainService).selectHighestPriorityPrice(Arrays.asList(mockPrice));
    }

    @Test
    @DisplayName("Debería lanzar PriceNotFoundException cuando no encuentra precios")
    void shouldThrowPriceNotFoundExceptionWhenNoPriceFound() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, 35455L, 1L);

        when(priceRepository.findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(applicationDate)))
                .thenReturn(Collections.emptyList());
        when(priceDomainService.selectHighestPriorityPrice(Collections.emptyList()))
                .thenReturn(Optional.empty());

        // When & Then
        PriceNotFoundException exception = assertThrows(
                PriceNotFoundException.class,
                () -> useCase.execute(request));

        assertTrue(exception.getMessage().contains("No se encontró precio aplicable"));
        assertTrue(exception.getMessage().contains("35455"));
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    @DisplayName("Debería seleccionar el precio con mayor prioridad cuando hay múltiples precios")
    void shouldSelectHighestPriorityWhenMultiplePrices() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, 35455L, 1L);
        
        Price basePrice = createMockPrice(1L, 0, BigDecimal.valueOf(35.50));
        Price promotionalPrice = createMockPrice(2L, 1, BigDecimal.valueOf(25.45));
        List<Price> prices = Arrays.asList(basePrice, promotionalPrice);

        when(priceRepository.findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(applicationDate)))
                .thenReturn(prices);
        when(priceDomainService.selectHighestPriorityPrice(prices))
                .thenReturn(Optional.of(promotionalPrice));

        // When
        PriceQueryResponseDTO result = useCase.execute(request);

        // Then
        assertNotNull(result);
        assertEquals(Integer.valueOf(2), result.getPriceList());
        assertEquals(BigDecimal.valueOf(25.45), result.getPrice());
    }

    @Test
    @DisplayName("Debería manejar correctamente diferentes fechas de aplicación")
    void shouldHandleDifferentApplicationDates() {
        // Given - Test para cada una de las 5 casuísticas
        LocalDateTime[] testDates = {
                LocalDateTime.of(2020, 6, 14, 10, 0),  // Caso 1
                LocalDateTime.of(2020, 6, 14, 16, 0),  // Caso 2
                LocalDateTime.of(2020, 6, 14, 21, 0),  // Caso 3
                LocalDateTime.of(2020, 6, 15, 10, 0),  // Caso 4
                LocalDateTime.of(2020, 6, 16, 21, 0)   // Caso 5
        };

        for (LocalDateTime date : testDates) {
            PriceQueryRequestDTO request = new PriceQueryRequestDTO(date, 35455L, 1L);
            Price mockPrice = createMockPrice(1L, 0, BigDecimal.valueOf(35.50));

            when(priceRepository.findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(date)))
                    .thenReturn(Arrays.asList(mockPrice));
            when(priceDomainService.selectHighestPriorityPrice(Arrays.asList(mockPrice)))
                    .thenReturn(Optional.of(mockPrice));

            // When
            PriceQueryResponseDTO result = useCase.execute(request);

            // Then
            assertNotNull(result);
            assertEquals(Long.valueOf(35455L), result.getProductId());
            assertEquals(Long.valueOf(1L), result.getBrandId());
        }
    }

    @Test
    @DisplayName("Debería validar correctamente los parámetros de entrada")
    void shouldValidateInputParameters() {
    	// Given
        LocalDateTime applicationDate = LocalDateTime.now();
        PriceQueryRequestDTO dto = new PriceQueryRequestDTO(null, null, null);

        // When
        Set<ConstraintViolation<PriceQueryRequestDTO>> violations = validator.validate(dto);

        
        

        // Then
        // Debe haber 3 errores
        assertEquals(3, violations.size(), "Debe haber 3 violaciones de validación");

        // Podemos comprobar que cada campo falla
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("applicationDate")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("productId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("brandId")));

    }

    @Test
    @DisplayName("Debería manejar repositorio que devuelve null")
    void shouldHandleRepositoryReturningNull() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, 35455L, 1L);

        when(priceRepository.findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(applicationDate)))
                .thenReturn(null);
        when(priceDomainService.selectHighestPriorityPrice(null))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(PriceNotFoundException.class, () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Debería manejar servicio de dominio que devuelve Optional vacío")
    void shouldHandleDomainServiceReturningEmpty() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceQueryRequestDTO request = new PriceQueryRequestDTO(applicationDate, 35455L, 1L);
        Price mockPrice = createMockPrice(1L, 0, BigDecimal.valueOf(35.50));

        when(priceRepository.findApplicablePrices(any(ProductId.class), any(BrandId.class), eq(applicationDate)))
                .thenReturn(Arrays.asList(mockPrice));
        when(priceDomainService.selectHighestPriorityPrice(Arrays.asList(mockPrice)))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(PriceNotFoundException.class, () -> useCase.execute(request));
    }

    private Price createMockPrice(Long id, Integer priorityValue, BigDecimal priceValue) {
        return Price.builder()
                .id(id)
                .brandId(new BrandId(1L))
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .priceList(id.intValue()) // Usamos el ID como priceList para diferenciación
                .productId(new ProductId(35455L))
                .priority(new Priority(priorityValue))
                .price(new Money(priceValue, "EUR"))
                .build();
    }
}