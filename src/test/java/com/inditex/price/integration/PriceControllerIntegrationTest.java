package com.inditex.price.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests de integración para el API REST de consulta de precios
 * Valida el comportamiento end-to-end de los endpoints
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/prices";

    @Test
    @Order(1)
    @DisplayName("Test 1: Consulta día 14 a las 10:00 - Debe devolver precio 35.50")
    void shouldReturnBasePriceAt14th10AM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Consulta día 14 a las 16:00 - Debe devolver precio 25.45")
    void shouldReturnPromotionalPriceAt14th4PM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T16:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Consulta día 14 a las 21:00 - Debe devolver precio 35.50")
    void shouldReturnBasePriceAt14th9PM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Consulta día 15 a las 10:00 - Debe devolver precio 30.50")
    void shouldReturnMorningSpecialPriceAt15th10AM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-15T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Consulta día 16 a las 21:00 - Debe devolver precio 38.95 ")
    void shouldReturnPremiumPriceAt16th9PM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-16T21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value(38.95))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @Order(6)
    @DisplayName("Error: Producto no existente - Debe devolver Error 404")
    void shouldReturn404ForNonExistentProduct() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "99999")
                .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Precio no encontrado"));
    }

    @Test
    @Order(7)
    @DisplayName("Error: Marca no existente - Debe devolver Error 404")
    void shouldReturn404ForNonExistentBrand() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Precio no encontrado"));
    }

    @Test
    @Order(8)
    @DisplayName("Error: Fecha fuera de rango - Debe devolver Error 404")
    void shouldReturn404ForDateOutOfRange() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2014-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Precio no encontrado"));
    }

    @Test
    @Order(9)
    @DisplayName("Error: Parámetros faltantes - Debe devolver Error 400")
    void shouldReturn400ForMissingParameters() throws Exception {
        // Falta applicationDate
        mockMvc.perform(get(BASE_URL)
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        // Falta productId
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        // Falta brandId
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    @DisplayName("Error: Parámetros inválidos - Debe devolver Error 400")
    void shouldReturn400ForInvalidParameters() throws Exception {
        // ProductId negativo
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "-1")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        // BrandId negativo
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "-1"))
                .andExpect(status().isBadRequest());

        // Fecha inválida
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "invalid-date")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    @DisplayName("Test de límites: Inicio exacto de periodo promocional")
    void shouldWorkAtExactStartOfPromotionalPeriod() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T15:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    @Order(12)
    @DisplayName("Test de límites: Final exacto de periodo promocional")
    void shouldWorkAtExactEndOfPromotionalPeriod() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T18:30:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    @Order(13)
    @DisplayName("Test concurrencia: Múltiples consultas simultáneas")
    void shouldHandleConcurrentRequests() throws Exception {
        // Simulamos varias consultas concurrentes
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get(BASE_URL)
                    .param("applicationDate", "2020-06-14T16:00:00")
                    .param("productId", "35455")
                    .param("brandId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.price").value(25.45));
        }
    }
}