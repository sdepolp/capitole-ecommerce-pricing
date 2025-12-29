package com.capitole.ecommerce.pricing_service.domain.exception;

import com.capitole.ecommerce.pricing_service.domain.model.PriceQuery;

/**
 * Exception thrown when no applicable price is found for the given criteria.
 * This is a domain exception that represents a business rule violation.
 */
public class PriceNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "No price found for the given criteria";

    public PriceNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public PriceNotFoundException(String message) {
        super(message);
    }

    public PriceNotFoundException(PriceQuery query) {
        super(String.format(
                "No price found for product %d, brand %d at date %s",
                query.productId(),
                query.brandId(),
                query.applicationDate()
        ));
    }

    public PriceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}