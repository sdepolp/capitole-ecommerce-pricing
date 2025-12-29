package com.capitole.ecommerce.pricing_service.application.port.in;


import com.capitole.ecommerce.pricing_service.domain.model.Price;
import com.capitole.ecommerce.pricing_service.domain.model.PriceQuery;
import com.capitole.ecommerce.pricing_service.domain.exception.PriceNotFoundException;
/**
 * Input port for retrieving applicable prices.
 * This interface defines the use case for finding the correct price
 * based on application date, product, and brand.
 * Following hexagonal architecture, this port is implemented by the application service
 * and called by the infrastructure adapters (e.g., REST controllers).
 */
public interface GetPriceUseCase {

    /**
     * Retrieves the applicable price for the given query criteria.
     * When multiple prices match the criteria, the one with the highest priority is returned.
     *
     * @param query The search criteria containing application date, product ID, and brand ID
     * @return The applicable price with the highest priority
     * @throws PriceNotFoundException if no price is found matching the criteria
     * @throws IllegalArgumentException if the query contains invalid data
     */
    Price getPrice(PriceQuery query);
}