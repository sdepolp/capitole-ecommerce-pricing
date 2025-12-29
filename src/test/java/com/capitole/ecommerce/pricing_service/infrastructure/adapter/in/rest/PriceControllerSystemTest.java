package com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * System tests for the Price API endpoint.
 * Tests the complete application stack with real database and all layers integrated.
 * These tests validate the 5 required scenarios from the specification.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Price Controller System Tests - Required Test Cases")
class PriceControllerSystemTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/prices";
    private static final Integer PRODUCT_ID = 35455;
    private static final Integer BRAND_ID = 1;

    @Test
    @DisplayName("Test 1: Request at 10:00 on June 14th - Should return price list 1 at 35.50 EUR")
    void test1_At10AMOnJune14_ShouldReturnPriceList1() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on June 14th - Should return price list 2 at 25.45 EUR")
    void test2_At4PMOnJune14_ShouldReturnPriceList2() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on June 14th - Should return price list 1 at 35.50 EUR")
    void test3_At9PMOnJune14_ShouldReturnPriceList1() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on June 15th - Should return price list 3 at 30.50 EUR")
    void test4_At10AMOnJune15_ShouldReturnPriceList3() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on June 16th - Should return price list 4 at 38.95 EUR")
    void test5_At9PMOnJune16_ShouldReturnPriceList4() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-15T21:00:00")
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value(38.95))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Should return 404 when no price is found")
    void shouldReturn404WhenNoPriceFound() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "99999")
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should return 400 when required parameters are missing")
    void shouldReturn400WhenParametersMissing() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", PRODUCT_ID.toString()))
                // Missing brandId
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when date format is invalid")
    void shouldReturn400WhenDateFormatInvalid() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "invalid-date")
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when product ID is negative")
    void shouldReturn400WhenProductIdNegative() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "-1")
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isBadRequest());
    }
}
