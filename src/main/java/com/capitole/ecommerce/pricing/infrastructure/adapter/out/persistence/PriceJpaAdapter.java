package com.capitole.ecommerce.pricing.infrastructure.adapter.out.persistence;

import com.capitole.ecommerce.pricing.application.port.out.PriceRepository;
import com.capitole.ecommerce.pricing.domain.model.Price;
import com.capitole.ecommerce.pricing.domain.model.PriceQuery;
import com.capitole.ecommerce.pricing.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.capitole.ecommerce.pricing.infrastructure.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.capitole.ecommerce.pricing.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * JPA adapter implementation of the PriceRepository port.
 * This adapter bridges the domain layer with the JPA persistence mechanism.
 * Implements the output port defined in the application layer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PriceJpaAdapter implements PriceRepository {

    private final PriceJpaRepository jpaRepository;
    private final PricePersistenceMapper mapper;

    @Override
    public Optional<Price> findApplicablePrice(PriceQuery query) {
        log.debug("Finding applicable price for query: {}", query);

        List<PriceEntity> entities = jpaRepository.findApplicablePrices(
                query.productId(),
                query.brandId(),
                query.applicationDate()
        );

        if (entities.isEmpty()) {
            log.debug("No prices found for query: {}", query);
            return Optional.empty();
        }

        // The query already orders by priority DESC, so we take the first one
        PriceEntity selectedEntity = entities.get(0);
        Price price = mapper.toDomain(selectedEntity);

        log.debug("Found applicable price: priceList={}, priority={}, price={}",
                price.priceList(), price.priority(), price.price());

        return Optional.of(price);
    }
}
