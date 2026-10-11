package com.example.tgtg.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * LEARN: Integration test. Boots the full app (controller -> service -> repository -> H2)
 * and fires fake HTTP requests through MockMvc. Slower, but tests the real wiring.
 * Alternative: @WebMvcTest(ProductController.class) + @MockitoBean ProductService
 * loads only the web layer.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    private String body(String name, String category) {
        LocalDateTime start = LocalDateTime.now().plusHours(2).withNano(0);
        return """
                {
                  "name": "%s",
                  "description": "Surprise bag",
                  "storeName": "Albert's Deli",
                  "category": "%s",
                  "originalPrice": 15.00,
                  "discountedPrice": 4.99,
                  "quantityAvailable": 5,
                  "pickupStart": "%s",
                  "pickupEnd": "%s"
                }
                """.formatted(name, category, start, start.plusHours(1));
    }

    @Test
    void postThenList() throws Exception {
        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("Pasta box", "MEALS")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/products/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Pasta box"));

        mvc.perform(get("/api/products").param("category", "MEALS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].name", hasItem("Pasta box")))
                .andExpect(jsonPath("$.page.totalElements").value(greaterThanOrEqualTo(1)));
    }

    @Test
    void postInvalidReturns400WithFieldErrors() throws Exception {
        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.storeName").exists());
    }

    @Test
    void getUnknownIdReturns404() throws Exception {
        mvc.perform(get("/api/products/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Product 9999 not found"));
    }
}
