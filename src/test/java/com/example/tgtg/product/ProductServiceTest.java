package com.example.tgtg.product;

import com.example.tgtg.common.BadRequestException;
import com.example.tgtg.common.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * LEARN: Pure unit test. No Spring context, so it runs in milliseconds.
 * The repository is a Mockito mock; we only test the service's own logic.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repository;

    ProductService service;

    // Fixed clock so "now" is deterministic
    final Clock clock = Clock.fixed(Instant.parse("2026-10-11T10:00:00Z"), ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        service = new ProductService(repository, clock);
    }

    private CreateProductRequest request(String original, String discounted) {
        LocalDateTime start = LocalDateTime.now(clock).plusHours(2);
        return new CreateProductRequest("Bread bag", "Mixed loaves", "Bakker Bart", Category.BAKERY,
                new BigDecimal(original), new BigDecimal(discounted), 3, start, start.plusHours(1));
    }

    @Test
    void create_savesAndReturnsProduct() {
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductResponse result = service.create(request("12.00", "3.99"));

        assertThat(result.name()).isEqualTo("Bread bag");
        assertThat(result.discountedPrice()).isEqualByComparingTo("3.99");
        verify(repository).save(any(Product.class));
    }

    @Test
    void create_rejectsDiscountNotLowerThanOriginal() {
        assertThatThrownBy(() -> service.create(request("5.00", "5.00")))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("discountedPrice");

        verifyNoInteractions(repository);
    }

    @Test
    void getById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L)).isInstanceOf(NotFoundException.class);
    }
}
