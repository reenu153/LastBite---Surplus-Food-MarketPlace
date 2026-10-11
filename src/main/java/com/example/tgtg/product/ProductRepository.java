package com.example.tgtg.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * LEARN: No implementation needed. Spring Data generates it at startup.
 * JpaRepository gives save, findById, findAll(Pageable), deleteById, count, ...
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // LEARN: "Derived query". Spring parses the method name into SQL:
    // WHERE quantity_available > ? AND pickup_end > ?
    Page<Product> findByQuantityAvailableGreaterThanAndPickupEndAfter(
            int quantity, LocalDateTime now, Pageable pageable);

    Page<Product> findByCategoryAndQuantityAvailableGreaterThanAndPickupEndAfter(
            Category category, int quantity, LocalDateTime now, Pageable pageable);
}
