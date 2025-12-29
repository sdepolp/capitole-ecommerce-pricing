package com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest.mapper;

import com.capitole.ecommerce.pricing_service.domain.model.Price;
import com.capitole.ecommerce.pricing_service.domain.model.PriceQuery;
import com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest.dto.PriceRequest;
import com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest.dto.PriceResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between REST DTOs and domain models.
 * Isolates the REST layer from the domain layer following hexagonal architecture.
 */
@Component
public class PriceRestMapper {

    /**
     * Converts a PriceRequest DTO to a PriceQuery domain object.
     *
     * @param request The REST request DTO
     * @return The domain query object
     */
    public PriceQuery toDomain(PriceRequest request) {
        if (request == null) {
            return null;
        }

        return new PriceQuery(
                request.getApplicationDate(),
                request.getProductId(),
                request.getBrandId()
        );
    }

    /**
     * Converts a Price domain object to a PriceResponse DTO.
     *
     * @param price The domain model
     * @return The REST response DTO
     */
    public PriceResponse toResponse(Price price) {
        if (price == null) {
            return null;
        }

        return PriceResponse.builder()
                .productId(price.productId())
                .brandId(price.brandId())
                .priceList(price.priceList())
                .startDate(price.startDate())
                .endDate(price.endDate())
                .price(price.price())
                .currency(price.currency())
                .build();
    }
}