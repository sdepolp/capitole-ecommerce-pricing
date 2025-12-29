package com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest;

import com.capitole.ecommerce.pricing_service.application.port.in.GetPriceUseCase;
import com.capitole.ecommerce.pricing_service.domain.model.Price;
import com.capitole.ecommerce.pricing_service.domain.model.PriceQuery;
import com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.capitole.ecommerce.pricing_service.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller for price queries.
 * Provides an HTTP endpoint to retrieve applicable prices based on date, product, and brand.
 */
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceRestMapper mapper;

    /**
     * Retrieves the applicable price for the given criteria.
     *
     * @param applicationDate The date and time when the price should be applicable (ISO format)
     * @param productId The product identifier
     * @param brandId The brand identifier
     * @return ResponseEntity containing the applicable price
     */
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam
            @NotNull(message = "Application date is required")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @RequestParam
            @NotNull(message = "Product ID is required")
            @Positive(message = "Product ID must be positive")
            Integer productId,

            @RequestParam
            @NotNull(message = "Brand ID is required")
            @Positive(message = "Brand ID must be positive")
            Integer brandId
    ) {
        log.info("GET /api/v1/prices - applicationDate={}, productId={}, brandId={}",
                applicationDate, productId, brandId);

        PriceQuery query = new PriceQuery(applicationDate, productId, brandId);
        Price price = getPriceUseCase.getPrice(query);
        PriceResponse response = mapper.toResponse(price);

        log.info("Returning price: priceList={}, price={}", response.getPriceList(), response.getPrice());

        return ResponseEntity.ok(response);
    }
}
