package com.example.tgtg.product;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * A "surprise bag" a store posts with leftover food at a discount.
 *
 * LEARN: An @Entity maps to a DB table. JPA needs a no-arg constructor
 * (protected is enough) and an @Id field.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-increments the id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String storeName; // ADAPT: becomes a @ManyToOne Store entity in a bigger version

    @Enumerated(EnumType.STRING) // store "BAKERY", not 0/1/2, so reordering the enum is safe
    @Column(nullable = false)
    private Category category;

    // LEARN: Use BigDecimal for money, never double (floating point rounding errors).
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountedPrice;

    @Column(nullable = false)
    private int quantityAvailable;

    @Column(nullable = false)
    private LocalDateTime pickupStart;

    @Column(nullable = false)
    private LocalDateTime pickupEnd;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Product() {
        // for JPA
    }

    public Product(String name, String description, String storeName, Category category,
                   BigDecimal originalPrice, BigDecimal discountedPrice, int quantityAvailable,
                   LocalDateTime pickupStart, LocalDateTime pickupEnd) {
        this.name = name;
        this.description = description;
        this.storeName = storeName;
        this.category = category;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.quantityAvailable = quantityAvailable;
        this.pickupStart = pickupStart;
        this.pickupEnd = pickupEnd;
    }

    // LEARN: JPA lifecycle callback, runs right before INSERT.
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStoreName() { return storeName; }
    public Category getCategory() { return category; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public BigDecimal getDiscountedPrice() { return discountedPrice; }
    public int getQuantityAvailable() { return quantityAvailable; }
    public LocalDateTime getPickupStart() { return pickupStart; }
    public LocalDateTime getPickupEnd() { return pickupEnd; }
    public Instant getCreatedAt() { return createdAt; }
}
