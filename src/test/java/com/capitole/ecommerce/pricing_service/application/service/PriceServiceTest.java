package com.capitole.ecommerce.pricing_service.application.service;

import com.capitole.ecommerce.pricing_service.application.port.out.PriceRepository;
import com.capitole.ecommerce.pricing_service.domain.exception.PriceNotFoundException;
import com.capitole.ecommerce.pricing_service.domain.model.Price;
import com.capitole.ecommerce.pricing_service.domain.model.PriceQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PriceService.
 * Uses Mockito to mock dependencies and test business logic in isolation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PriceService Unit Tests")
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceService priceService;

    @Test
    @DisplayName("Should return price when repository finds one")
    void shouldReturnPriceWhenFound() {
        // Given
        PriceQuery query = new PriceQuery(
                LocalDateTime.of(2020, 6, 14, 10, 0),
                35455,
                1
        );

        Price expectedPrice = new Price(
                35455,
                1,
                1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                new BigDecimal("35.50"),
                "EUR",
                0
        );

        when(priceRepository.findApplicablePrice(query)).thenReturn(Optional.of(expectedPrice));

        // When
        Price result = priceService.getPrice(query);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedPrice);
        verify(priceRepository, times(1)).findApplicablePrice(query);
    }

    @Test
    @DisplayName("Should throw PriceNotFoundException when repository returns empty")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        PriceQuery query = new PriceQuery(
                LocalDateTime.of(2020, 6, 14, 10, 0),
                99999,
                1
        );

        when(priceRepository.findApplicablePrice(query)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> priceService.getPrice(query))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("No price found");

        verify(priceRepository, times(1)).findApplicablePrice(query);
    }

    @Test
    @DisplayName("Should propagate validation exceptions from query")
    void shouldPropagateValidationExceptions() {
        // Given - Invalid query with null date
        // When & Then
        assertThatThrownBy(() -> new PriceQuery(null, 35455, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Application date cannot be null");

        verify(priceRepository, never()).findApplicablePrice(any());
    }

    @Test
    @DisplayName("Should call repository with correct parameters")
    void shouldCallRepositoryWithCorrectParameters() {
        // Given
        LocalDateTime testDate = LocalDateTime.of(2020, 6, 15, 16, 0);
        Integer testProductId = 35455;
        Integer testBrandId = 1;

        PriceQuery query = new PriceQuery(testDate, testProductId, testBrandId);

        Price mockPrice = new Price(
                testProductId,
                testBrandId,
                4,
                LocalDateTime.of(2020, 6, 15, 16, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                new BigDecimal("38.95"),
                "EUR",
                1
        );

        when(priceRepository.findApplicablePrice(query)).thenReturn(Optional.of(mockPrice));

        // When
        Price result = priceService.getPrice(query);

        // Then
        assertThat(result.productId()).isEqualTo(testProductId);
        assertThat(result.brandId()).isEqualTo(testBrandId);
        verify(priceRepository).findApplicablePrice(argThat(q ->
                q.applicationDate().equals(testDate) &&
                        q.productId().equals(testProductId) &&
                        q.brandId().equals(testBrandId)
        ));
    }
}