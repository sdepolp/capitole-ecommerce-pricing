package com.capitole.ecommerce.pricing.domain.model;

import java.time.LocalDateTime;

/**
 * Value object representing a query for finding applicable prices.
 * Encapsulates the criteria needed to search for a price.
 *
 * @param applicationDate The date and time when the price should be applicable
 * @param productId The unique identifier of the product
 * @param brandId The unique identifier of the brand/chain
 */
public record PriceQuery(
        LocalDateTime applicationDate,
        Integer productId,
        Integer brandId
) {
    /**
     * Compact constructor with validation.
     */
    public PriceQuery {
        if (applicationDate == null) {
            throw new IllegalArgumentException("Application date cannot be null");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Brand ID must be positive");
        }
    }
}