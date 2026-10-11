package com.example.tgtg.product;

import com.example.tgtg.common.BadRequestException;
import com.example.tgtg.common.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * LEARN: Business logic lives here, not in the controller.
 * Controller = HTTP concerns, Service = rules, Repository = persistence.
 */
@Service
public class ProductService {

    private final ProductRepository repository;
    private final Clock clock;

    // LEARN: Constructor injection (preferred over @Autowired on fields):
    // dependencies are final, explicit, and easy to mock in unit tests.
    // With a single constructor, Spring injects automatically, no @Autowired needed.
    public ProductService(ProductRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest req) {
        // Cross-field rules that simple annotations can't express:
        if (req.discountedPrice().compareTo(req.originalPrice()) >= 0) {
            throw new BadRequestException("discountedPrice must be lower than originalPrice");
        }
        if (!req.pickupEnd().isAfter(req.pickupStart())) {
            throw new BadRequestException("pickupEnd must be after pickupStart");
        }

        Product saved = repository.save(new Product(
                req.name(), req.description(), req.storeName(), req.category(),
                req.originalPrice(), req.discountedPrice(), req.quantityAvailable(),
                req.pickupStart(), req.pickupEnd()));

        return ProductResponse.from(saved);
    }

    /** Lists bags that are still in stock and whose pickup window hasn't ended. */
    @Transactional(readOnly = true)
    public Page<ProductResponse> listAvailable(Category category, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now(clock);
        Page<Product> page = (category == null)
                ? repository.findByQuantityAvailableGreaterThanAndPickupEndAfter(0, now, pageable)
                : repository.findByCategoryAndQuantityAvailableGreaterThanAndPickupEndAfter(category, 0, now, pageable);
        return page.map(ProductResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return repository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
    }
}
