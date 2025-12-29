package com.capitole.ecommerce.pricing.application.service;

import com.capitole.ecommerce.pricing.application.port.in.GetPriceUseCase;
import com.capitole.ecommerce.pricing.application.port.out.PriceRepository;
import com.capitole.ecommerce.pricing.domain.exception.PriceNotFoundException;
import com.capitole.ecommerce.pricing.domain.model.Price;
import com.capitole.ecommerce.pricing.domain.model.PriceQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service implementing the GetPrice use case.
 * This service orchestrates the business logic for retrieving applicable prices.
 * It acts as the boundary between the domain and the infrastructure layers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceService implements GetPriceUseCase {

    private final PriceRepository priceRepository;

    @Override
    @Transactional(readOnly = true)
    public Price getPrice(PriceQuery query) {
        log.info("Getting price for productId={}, brandId={}, date={}",
                query.productId(), query.brandId(), query.applicationDate());

        return priceRepository.findApplicablePrice(query)
                .orElseThrow(() -> {
                    log.warn("No price found for query: {}", query);
                    return new PriceNotFoundException(query);
                });
    }
}
