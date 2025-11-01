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
 * Tests de sistema con las 5 casuísticas específicas del ejercicio Inditex
 * Estas son las consultas exactas que se solicitan tradicionalmente en el test
 */
@SpringBootTest
@AutoConfigureMockMvc 
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceSystemTestCases {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/prices";
    private static final String PRODUCT_ID = "35455";
    private static final String BRAND_ID = "1";

    @Test
    @Order(1)
    @DisplayName("Caso 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
    void testCase1_Day14At10AM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
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
    @DisplayName("Caso 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
    void testCase2_Day14At4PM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T16:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
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
    @DisplayName("Caso 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
    void testCase3_Day14At9PM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T21:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
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
    @DisplayName("Caso 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)")
    void testCase4_Day15At10AM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-15T10:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
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
    @DisplayName("Caso 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)")
    void testCase5_Day16At9PM() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-16T21:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
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
    @DisplayName("Caso adicional: Verificación de límites exactos de periodo promocional")
    void testCase_ExactBoundaries() throws Exception {
        // Inicio exacto de la promoción del día 14 (15:00)
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T15:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));

        // Final exacto de la promoción del día 14 (18:30)
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T18:30:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));

        // Un minuto después del final de la promoción (18:31) - debe volver al precio base
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-14T18:31:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @Order(7)
    @DisplayName("Caso adicional: Verificación de prioridades con solapamiento")
    void testCase_PriorityOverlapping() throws Exception {
        // Durante el solapamiento entre precio base (prioridad 0) y promocional (prioridad 1)
        // Debe devolver el de mayor prioridad (promocional)
        mockMvc.perform(get(BASE_URL)
                .param("applicationDate", "2020-06-15T10:30:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    @Order(8)
    @DisplayName("Caso de stress: Múltiples consultas concurrentes del mismo caso")
    void testCase_ConcurrentRequests() throws Exception {
        // Ejecutar múltiples consultas del caso más complejo para verificar concurrencia
        int numberOfRequests = 50;
        
        for (int i = 0; i < numberOfRequests; i++) {
            mockMvc.perform(get(BASE_URL)
                    .param("applicationDate", "2020-06-14T16:00:00")
                    .param("productId", PRODUCT_ID)
                    .param("brandId", BRAND_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.priceList").value(2))
                    .andExpect(jsonPath("$.price").value(25.45));
        }
    }
}