package com.capitole.ecommerce.pricing_service.infrastructure.adapter.out.persistence.repository;

import com.capitole.ecommerce.pricing_service.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for price entities.
 * Provides database access methods for price queries.
 */
@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Finds all prices that match the given criteria and are applicable at the specified date.
     * Results are ordered by priority in descending order (highest priority first).
     *
     * @param productId The product identifier
     * @param brandId The brand identifier
     * @param applicationDate The date to check for price applicability
     * @return List of matching prices ordered by priority DESC
     */
    @Query("""
        SELECT p FROM PriceEntity p
        WHERE p.productId = :productId
          AND p.brandId = :brandId
          AND :applicationDate BETWEEN p.startDate AND p.endDate
        ORDER BY p.priority DESC
        """)
    List<PriceEntity> findApplicablePrices(
            @Param("productId") Integer productId,
            @Param("brandId") Integer brandId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}
