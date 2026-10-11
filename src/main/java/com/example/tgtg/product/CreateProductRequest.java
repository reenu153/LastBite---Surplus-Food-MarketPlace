package com.example.tgtg.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request body for POST /api/products.
 *
 * LEARN: A DTO (Data Transfer Object) separates the API contract from the DB entity.
 * Clients can't set id/createdAt, and you can change the table without breaking the API.
 * Java records are immutable and perfect for DTOs.
 */
public record CreateProductRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotBlank String storeName,
        @NotNull Category category,
        @NotNull @Positive BigDecimal originalPrice,
        @NotNull @Positive BigDecimal discountedPrice,
        @Min(1) int quantityAvailable,
        @NotNull @Future LocalDateTime pickupStart,
        @NotNull @Future LocalDateTime pickupEnd
) {
}
