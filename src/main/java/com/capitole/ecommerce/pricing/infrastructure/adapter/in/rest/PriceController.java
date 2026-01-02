package com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest;

import com.capitole.ecommerce.pricing.application.port.in.GetPriceUseCase;
import com.capitole.ecommerce.pricing.domain.model.Price;
import com.capitole.ecommerce.pricing.domain.model.PriceQuery;
import com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest.dto.ErrorResponse;
import com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Prices", description = "Price query operations for e-commerce products")
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
    @Operation(
            summary = "Get applicable price",
            description = "Retrieves the applicable price for a product at a specific date and time. " +
                    "When multiple prices match the criteria, the one with the highest priority is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Price found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PriceResponse.class),
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    value = """
                        {
                          "productId": 35455,
                          "brandId": 1,
                          "priceList": 1,
                          "startDate": "2020-06-14T00:00:00",
                          "endDate": "2020-12-31T23:59:59",
                          "price": 35.50,
                          "currency": "EUR"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Bad Request Example",
                                    value = """
                        {
                          "timestamp": "2024-12-23T10:30:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Product ID must be positive",
                          "path": "/api/v1/prices"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No price found for the given criteria",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Not Found Example",
                                    value = """
                        {
                          "timestamp": "2024-12-23T10:30:00",
                          "status": 404,
                          "error": "Not Found",
                          "message": "No price found for product 35455, brand 1 at date 2020-06-14T10:00:00",
                          "path": "/api/v1/prices"
                        }
                        """
                            )
                    )
            )
    })
    public ResponseEntity<PriceResponse> getPrice(
            @Parameter(
                    description = "Date and time when the price should be applicable (ISO 8601 format)",
                    required = true,
                    example = "2020-06-14T10:00:00"
            )
            @RequestParam
            @NotNull(message = "Application date is required")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @Parameter(
                    description = "Product identifier",
                    required = true,
                    example = "35455"
            )
            @RequestParam
            @NotNull(message = "Product ID is required")
            @Positive(message = "Product ID must be positive")
            Integer productId,

            @Parameter(
                    description = "Brand identifier (1 = ZARA)",
                    required = true,
                    example = "1"
            )
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