package com.capitole.ecommerce.pricing.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PriceResponse {

    private Integer productId;

    private Integer brandId;

    private Integer priceList;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    private BigDecimal price;

    private String currency;
}