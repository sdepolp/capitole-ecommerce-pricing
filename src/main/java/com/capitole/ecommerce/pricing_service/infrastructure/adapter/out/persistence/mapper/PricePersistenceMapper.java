package com.capitole.ecommerce.pricing_service.infrastructure.adapter.out.persistence.mapper;

import com.capitole.ecommerce.pricing_service.domain.model.Price;
import com.capitole.ecommerce.pricing_service.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Price domain model and PriceEntity.
 * Follows the hexagonal architecture pattern by isolating domain from infrastructure.
 */
@Component
public class PricePersistenceMapper {

    /**
     * Converts a PriceEntity to a Price domain object.
     *
     * @param entity The JPA entity
     * @return The domain model
     */
    public Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Price(
                entity.getProductId(),
                entity.getBrandId(),
                entity.getPriceList(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getPriority()
        );
    }

    /**
     * Converts a Price domain object to a PriceEntity.
     * Note: This is typically not needed for read-only operations.
     *
     * @param price The domain model
     * @return The JPA entity
     */
    public PriceEntity toEntity(Price price) {
        if (price == null) {
            return null;
        }

        return PriceEntity.builder()
                .productId(price.productId())
                .brandId(price.brandId())
                .priceList(price.priceList())
                .startDate(price.startDate())
                .endDate(price.endDate())
                .price(price.price())
                .currency(price.currency())
                .priority(price.priority())
                .build();
    }
}
