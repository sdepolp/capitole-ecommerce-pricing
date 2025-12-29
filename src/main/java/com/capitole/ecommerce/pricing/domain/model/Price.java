package com.capitole.ecommerce.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a price for a product in a specific brand and time range.
 * This is the core business entity following Domain-Driven Design principles.
 *
 * @param productId Unique identifier of the product
 * @param brandId Unique identifier of the brand/chain (e.g., 1 = ZARA)
 * @param priceList Identifier of the applicable price list/tariff
 * @param startDate Start date and time when this price becomes effective
 * @param endDate End date and time when this price expires
 * @param price Final sale price to apply
 * @param currency ISO currency code (e.g., EUR, USD)
 * @param priority Disambiguation value for applying prices when multiple match (higher value = higher priority)
 */
public record Price(
        Integer productId,
        Integer brandId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency,
        Integer priority
) {
    /**
     * Compact constructor with validation.
     * Ensures all required fields are present and valid.
     */
    public Price {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Brand ID must be positive");
        }
        if (priceList == null) {
            throw new IllegalArgumentException("Price list cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
    }

    /**
     * Checks if this price is applicable for the given date.
     *
     * @param applicationDate The date to check
     * @return true if the date falls within the price's validity period
     */
    public boolean isApplicableAt(LocalDateTime applicationDate) {
        return !applicationDate.isBefore(startDate) && !applicationDate.isAfter(endDate);
    }
}