package com.capitole.ecommerce.pricing.application.port.out;

import com.capitole.ecommerce.pricing.domain.model.Price;
import com.capitole.ecommerce.pricing.domain.model.PriceQuery;

import java.util.Optional;

/**
 * Output port for price persistence operations.
 * This interface defines the contract for retrieving prices from the data store.
 *
 * Following hexagonal architecture, this port is implemented by infrastructure adapters
 * (e.g., JPA adapter) and called by the application service.
 */
public interface PriceRepository {

    /**
     * Finds the applicable price for the given query criteria.
     * If multiple prices match, returns the one with the highest priority.
     *
     * The implementation should:
     * 1. Find all prices matching productId and brandId
     * 2. Filter those where applicationDate is between startDate and endDate
     * 3. Return the one with highest priority
     *
     * @param query The search criteria containing application date, product ID, and brand ID
     * @return Optional containing the applicable price with highest priority, or empty if none found
     */
    Optional<Price> findApplicablePrice(PriceQuery query);
}