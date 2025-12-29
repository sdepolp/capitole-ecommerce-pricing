package com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for price queries.
 * This class is used to receive query parameters from the REST API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {

    @NotNull(message = "Application date is required")
    private LocalDateTime applicationDate;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Integer productId;

    @NotNull(message = "Brand ID is required")
    @Positive(message = "Brand ID must be positive")
    private Integer brandId;
}