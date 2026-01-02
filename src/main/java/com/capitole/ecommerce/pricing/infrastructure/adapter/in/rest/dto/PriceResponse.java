package com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for price queries.
 * This class represents the data returned by the REST API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Price information for a product")
public class PriceResponse {

    @Schema(description = "Product identifier", example = "35455")
    private Integer productId;

    @Schema(description = "Brand identifier (1 = ZARA)", example = "1")
    private Integer brandId;

    @Schema(description = "Price list identifier", example = "1")
    private Integer priceList;

    @Schema(description = "Start date of price validity", example = "2020-06-14T00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "End date of price validity", example = "2020-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @Schema(description = "Final sale price", example = "35.50")
    private BigDecimal price;

    @Schema(description = "Currency code (ISO 4217)", example = "EUR")
    private String currency;
}