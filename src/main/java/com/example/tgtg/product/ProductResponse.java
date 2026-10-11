package com.example.tgtg.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/** What the API returns. Never expose the entity directly. */
public record ProductResponse(
        Long id,
        String name,
        String description,
        String storeName,
        Category category,
        BigDecimal originalPrice,
        BigDecimal discountedPrice,
        int quantityAvailable,
        LocalDateTime pickupStart,
        LocalDateTime pickupEnd,
        Instant createdAt
) {
    // LEARN: static factory keeps mapping in one place (alternative: MapStruct).
    static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(), p.getName(), p.getDescription(), p.getStoreName(), p.getCategory(),
                p.getOriginalPrice(), p.getDiscountedPrice(), p.getQuantityAvailable(),
                p.getPickupStart(), p.getPickupEnd(), p.getCreatedAt());
    }
}
