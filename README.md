# TGTG Backend (Spring Boot learning project)

A minimal Too Good To Go style backend: stores post discounted "surprise bags", users list the ones still available.

Stack: Java 21, Spring Boot 3.5, Spring Web, Spring Data JPA, Bean Validation, PostgreSQL 16 (Docker). Tests use in-memory H2.

## Run

```bash
docker compose up -d       # start Postgres (wait a few seconds for it to be healthy)
mvn spring-boot:run        # or run TgtgApplication from IntelliJ
mvn test                   # unit + integration tests (H2, no Docker needed)
docker compose down        # stop Postgres (data is kept; `down -v` wipes it)
```

Needs JDK 21, Maven (`brew install openjdk@21 maven`) and Docker Desktop. App starts on http://localhost:8080.
Inspect the DB: `docker exec -it tgtg-postgres psql -U tgtg -d tgtg`, then `select * from products;`
Or connect IntelliJ/DBeaver to `localhost:5432`, db `tgtg`, user `tgtg`, password `tgtg`.

## Endpoints

| Method | Path | What |
|---|---|---|
| POST | `/api/products` | Post a surprise bag (201 + Location header) |
| GET | `/api/products` | List available bags (in stock, pickup not ended). Optional `category`, `page`, `size`, `sort` |
| GET | `/api/products/{id}` | One bag (404 if missing) |

```bash
curl -i -X POST localhost:8080/api/products -H 'Content-Type: application/json' -d '{
  "name": "Bread bag",
  "description": "Mixed loaves from today",
  "storeName": "Bakker Bart",
  "category": "BAKERY",
  "originalPrice": 12.00,
  "discountedPrice": 3.99,
  "quantityAvailable": 4,
  "pickupStart": "2026-12-01T18:00:00",
  "pickupEnd": "2026-12-01T19:00:00"
}'

curl 'localhost:8080/api/products?category=BAKERY&sort=discountedPrice,asc&size=5'
```

## How a request flows

```
HTTP -> ProductController -> ProductService -> ProductRepository -> Postgres
         (DTO + @Valid)       (business rules)   (Spring Data JPA)
errors  -> GlobalExceptionHandler -> ProblemDetail JSON (400 / 404)
```

Package-by-feature: everything for products lives in `product/`, shared stuff in `common/`.
Search the code for `LEARN:` (concept explanations) and `ADAPT:` (where to extend).

## Interview talking points this project covers

- **IoC / DI**: Spring creates beans (`@Service`, `@RestController`, `@Bean`) and injects them via constructors.
- **Layering**: controller (HTTP), service (rules, `@Transactional`), repository (data).
- **DTO vs Entity**: records for request/response, so the DB model never leaks into the API.
- **Validation**: annotations on the DTO + cross-field rules in the service.
- **Spring Data**: derived queries from method names, `Pageable` for paging and sorting.
- **Error handling**: `@RestControllerAdvice` + RFC 7807 `ProblemDetail`.
- **Testing**: Mockito unit test (fast, no Spring) vs `@SpringBootTest` + MockMvc (full stack).
- **Testable time**: inject a `Clock` instead of calling `now()` directly.

## Next steps to practise (likely follow-up questions)

1. Add a `Store` entity with `@ManyToOne` from Product (watch for the N+1 problem, fix with `@EntityGraph` or `JOIN FETCH`).
2. Add `POST /api/products/{id}/reservations` that decrements stock. Discuss race conditions: `@Version` optimistic locking.
3. Add Flyway migrations (`ddl-auto=validate`) and Testcontainers so tests also run on Postgres.
4. Add Spring Security with JWT so only store accounts can POST.
5. Add `PUT`/`DELETE` endpoints and a `@WebMvcTest` slice test.
# LastBite---Surplus-Food-MarketPlace
