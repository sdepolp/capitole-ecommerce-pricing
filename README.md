# Pricing Service

E-commerce pricing service built with hexagonal architecture, providing REST API endpoints to query applicable prices based on date, product, and brand criteria.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database](#database)
- [Design Decisions](#design-decisions)

---

## Overview

This service provides price information for products in an e-commerce platform. It handles multiple price lists with different priorities and date ranges, automatically selecting the most appropriate price based on business rules.

### Business Rules

- Prices are associated with a brand, product, and validity date range
- Multiple prices can overlap in time
- When multiple prices apply, the one with the **highest priority** is selected
- All queries require: application date, product ID, and brand ID

---

## Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters pattern), ensuring clean separation of concerns and high testability.

```
┌─────────────────────────────────────────────────────────────┐
│                     Infrastructure Layer                      │
│  ┌──────────────────┐              ┌──────────────────┐     │
│  │   REST API       │              │   Persistence    │     │
│  │   (Adapter In)   │              │   (Adapter Out)  │     │
│  └────────┬─────────┘              └─────────┬────────┘     │
│           │                                   │              │
│           │         ┌──────────────┐          │              │
│           └────────>│ Application  │<─────────┘              │
│                     │   Service    │                         │
│                     └──────┬───────┘                         │
│                            │                                 │
│                     ┌──────▼───────┐                         │
│                     │    Domain    │                         │
│                     │  (Business   │                         │
│                     │    Logic)    │                         │
│                     └──────────────┘                         │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

#### Domain Layer
- **Pure business logic**, no framework dependencies
- Contains entities (`Price`), value objects (`PriceQuery`), and exceptions
- Uses Java 21 Records for immutability

#### Application Layer
- **Use cases** and business workflows
- Defines **ports** (interfaces) for external interactions
- Orchestrates domain objects

#### Infrastructure Layer
- **Adapters** implementing the ports
- REST controllers, JPA repositories, mappers
- Framework-specific code (Spring, JPA, etc.)

---

## Technologies

- **Java 21** - Latest LTS with Records, Pattern Matching, Text Blocks
- **Spring Boot 3.4.1** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Flyway** - Database migrations
- **Lombok** - Boilerplate reduction
- **JUnit 5** - Testing framework
- **MockMvc** - REST API testing
- **Maven** - Build tool

---

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/sdepolp/capitole-ecommerce-pricing.git
cd capitole-ecommerce-pricing
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Quick Test

```bash
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

Expected response:
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "currency": "EUR"
}
```

---

## API Documentation

### Get Applicable Price

Retrieves the applicable price for a product at a specific date and time.

**Endpoint:** `GET /api/v1/prices`

**Query Parameters:**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `applicationDate` | ISO DateTime | Yes | Date and time to check price | `2020-06-14T10:00:00` |
| `productId` | Integer | Yes | Product identifier | `35455` |
| `brandId` | Integer | Yes | Brand identifier (1 = ZARA) | `1` |

**Success Response (200 OK):**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "currency": "EUR"
}
```

**Error Responses:**

- **404 Not Found** - No price found for the given criteria
```json
{
  "timestamp": "2024-12-23T01:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No price found for product 35455, brand 1 at date 2020-06-14T10:00:00",
  "path": "/api/v1/prices"
}
```

- **400 Bad Request** - Invalid parameters
```json
{
  "timestamp": "2024-12-23T01:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Product ID must be positive",
  "path": "/api/v1/prices"
}
```

### API Examples

```bash
# Test 1: June 14 at 10:00 - Returns price list 1 (35.50 EUR)
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"

# Test 2: June 14 at 16:00 - Returns price list 2 (25.45 EUR)
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"

# Test 3: June 14 at 21:00 - Returns price list 1 (35.50 EUR)
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"

# Test 4: June 15 at 10:00 - Returns price list 3 (30.50 EUR)
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"

# Test 5: June 15 at 21:00 - Returns price list 4 (38.95 EUR)
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-15T21:00:00&productId=35455&brandId=1"
```

---

## Testing

The project includes comprehensive testing at all levels:

### Run All Tests

```bash
mvn test
```

### Test Coverage

- **Unit Tests**: Service layer with mocked dependencies
- **Integration Tests**: Persistence layer with embedded H2
- **System Tests**: Full application stack with REST API

### Test Structure

```
src/test/java/
├── application/
│   └── service/
│       └── PriceServiceTest.java           # Unit tests
├── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   └── rest/
    │   │       └── PriceControllerSystemTest.java  # System tests (5 required scenarios)
    │   └── out/
    │       └── persistence/
    │           └── PriceJpaAdapterTest.java        # Integration tests
```

### Required Test Scenarios (All Passing)

1. ✅ Request at 10:00 on June 14th → Returns price list 1 at 35.50 EUR
2. ✅ Request at 16:00 on June 14th → Returns price list 2 at 25.45 EUR
3. ✅ Request at 21:00 on June 14th → Returns price list 1 at 35.50 EUR
4. ✅ Request at 10:00 on June 15th → Returns price list 3 at 30.50 EUR
5. ✅ Request at 21:00 on June 15th → Returns price list 4 at 38.95 EUR

---

## Database

### H2 Console

Access the H2 console at: `http://localhost:8080/h2-console`

**Connection Settings:**
- **JDBC URL**: `jdbc:h2:mem:pricingdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

### Schema

```sql
CREATE TABLE prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price_list INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    priority INTEGER NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    curr VARCHAR(3) NOT NULL
);
```

### Sample Data

The database is initialized with 4 price records for product 35455 (ZARA):

| Brand | Product | Price List | Start Date | End Date | Priority | Price | Currency |
|-------|---------|------------|------------|----------|----------|-------|----------|
| 1 | 35455 | 1 | 2020-06-14 00:00 | 2020-12-31 23:59 | 0 | 35.50 | EUR |
| 1 | 35455 | 2 | 2020-06-14 15:00 | 2020-06-14 18:30 | 1 | 25.45 | EUR |
| 1 | 35455 | 3 | 2020-06-15 00:00 | 2020-06-15 11:00 | 1 | 30.50 | EUR |
| 1 | 35455 | 4 | 2020-06-15 16:00 | 2020-12-31 23:59 | 1 | 38.95 | EUR |

### Flyway Migrations

Database schema and data are managed via Flyway migrations:

- `V1__create_prices_table.sql` - Creates the schema
- `V2__insert_initial_data.sql` - Inserts test data

---

## Design Decisions

### 1. Hexagonal Architecture
- **Why**: Clear separation of concerns, highly testable, framework-independent domain
- **Benefit**: Easy to swap implementations (e.g., change database, add new adapters)

### 2. Java 21 Records
- **Why**: Immutability, conciseness, built-in equals/hashCode/toString
- **Usage**: Domain entities (`Price`, `PriceQuery`)

### 3. Constructor Injection
- **Why**: Immutability, easier testing, explicit dependencies
- **Pattern**: All services use `@RequiredArgsConstructor` with final fields

### 4. Query Ordering in Database
- **Why**: Performance - let the database do the sorting
- **Implementation**: JPQL query orders by priority DESC, take first result

### 5. Optional<> for Repository
- **Why**: Explicit handling of "not found" cases
- **Benefit**: Forces explicit null handling, prevents NullPointerException

### 6. Separate DTOs and Domain Models
- **Why**: Isolation of layers, API evolution without affecting domain
- **Benefit**: Can change API contract without touching business logic

### 7. Global Exception Handler
- **Why**: Consistent error responses across all endpoints
- **Implementation**: `@RestControllerAdvice` with specific exception handlers

### 8. Flyway for Migrations
- **Why**: Version control for database, repeatable deployments
- **Benefit**: Database schema is code, tracked in Git

---

## Project Structure

```
pricing-service/
├── src/
│   ├── main/
│   │   ├── java/com/capitole/ecommerce/pricing_service/
│   │   │   ├── PricingServiceApplication.java
│   │   │   ├── application/
│   │   │   │   ├── port/
│   │   │   │   │   ├── in/
│   │   │   │   │   │   └── GetPriceUseCase.java
│   │   │   │   │   └── out/
│   │   │   │   │       └── PriceRepository.java
│   │   │   │   └── service/
│   │   │   │       └── PriceService.java
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── Price.java
│   │   │   │   │   └── PriceQuery.java
│   │   │   │   └── exception/
│   │   │   │       └── PriceNotFoundException.java
│   │   │   └── infrastructure/
│   │   │       ├── adapter/
│   │   │       │   ├── in/
│   │   │       │   │   ├── rest/
│   │   │       │   │   │   ├── PriceController.java
│   │   │       │   │   │   ├── dto/
│   │   │       │   │   │   │   ├── PriceRequest.java
│   │   │       │   │   │   │   ├── PriceResponse.java
│   │   │       │   │   │   │   └── ErrorResponse.java
│   │   │       │   │   │   └── mapper/
│   │   │       │   │   │       └── PriceRestMapper.java
│   │   │       │   │   └── exception/
│   │   │       │   │       └── GlobalExceptionHandler.java
│   │   │       │   └── out/
│   │   │       │       └── persistence/
│   │   │       │           ├── PriceJpaAdapter.java
│   │   │       │           ├── entity/
│   │   │       │           │   └── PriceEntity.java
│   │   │       │           ├── repository/
│   │   │       │           │   └── PriceJpaRepository.java
│   │   │       │           └── mapper/
│   │   │       │               └── PricePersistenceMapper.java
│   │   │       └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/
│   │           └── migration/
│   │               ├── V1__create_prices_table.sql
│   │               └── V2__insert_initial_data.sql
│   └── test/
│       ├── java/...
│       └── resources/
│           └── application-test.yml
├── pom.xml
└── README.md
```

---

## Configuration

### Application Properties

The application can be configured via `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:pricingdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
  
  flyway:
    enabled: true
    baseline-on-migrate: true
```

---
### 📚 Interactive API Documentation (Swagger UI)

The API is documented using OpenAPI 3.0 specification and can be explored interactively through Swagger UI.

**Access Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

**OpenAPI JSON Specification:**
```
http://localhost:8080/api-docs
```

**Features:**
- 🔍 Explore all available endpoints
- 📝 View request/response schemas
- ▶️ Try out API calls directly from the browser
- 📋 See example requests and responses
- 📊 View all data models and validations

**Try it out:**
1. Start the application: `mvn spring-boot:run`
2. Open browser: http://localhost:8080/swagger-ui.html
3. Expand the "Prices" section
4. Click "Try it out"
5. Fill in the parameters with test data
6. Click "Execute" to see the response

---

## Author

**Santiago De Pol Pinto** - Technical Assessment for Capitole

---

## License

This project is developed as a technical assessment.

---

## Acknowledgments

- Clean Architecture principles by Robert C. Martin
- Hexagonal Architecture (Ports and Adapters) by Alistair Cockburn
- Spring Boot best practices and conventions
