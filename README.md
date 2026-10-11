

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


