# Inventory Service

A Spring Boot REST API application for managing customer inventory with full CRUD operations, pagination, and comprehensive validation.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Relational database
- **Maven** - Build and dependency management
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Bean validation
- **Spring Boot Actuator** - Application monitoring

## Features

- ✅ Complete CRUD operations for Customer management
- ✅ RESTful API design with standardized responses
- ✅ Pagination and sorting support
- ✅ Comprehensive input validation
- ✅ Global exception handling
- ✅ Health checks and monitoring endpoints
- ✅ PostgreSQL database integration
- ✅ Timestamps (createdAt, updatedAt) for audit trail

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ running on port 5435
- Database: `inventory-service-db`
- Database credentials: `postgres` / `postgres@16`

## Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd inventory-service
```

### 2. Setup Database
Make sure PostgreSQL is running and create the database:
```sql
CREATE DATABASE "inventory-service-db";
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/inventory-service-0.0.1-SNAPSHOT.jar
```

The application will start on: `http://localhost:8080/inventory-service`

## API Documentation

### Base URL
```
http://localhost:8080/inventory-service
```

### Customer Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/customers` | Create a new customer |
| GET | `/api/v1/customers/{id}` | Get customer by ID |
| GET | `/api/v1/customers` | Get all customers (paginated) |
| PUT | `/api/v1/customers/{id}` | Update a customer |
| DELETE | `/api/v1/customers/{id}` | Delete a customer |

### Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/management/health` | Health check |
| GET | `/management/metrics` | Application metrics |
| GET | `/management/info` | Application info |
| GET | `/management/env` | Environment properties |

## Request/Response Examples

### Create Customer
**Request:**
```bash
POST /api/v1/customers
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main Street, New York, NY 10001"
}
```

**Response:** `201 Created`
```json
{
    "metaData": {
        "status": "SUCCESS"
    },
    "data": {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "address": "123 Main Street, New York, NY 10001"
    }
}
```

### Get All Customers (Paginated)
**Request:**
```bash
GET /api/v1/customers?page=0&size=10&sort=lastName,asc
```

**Response:** `200 OK`
```json
{
    "metaData": {
        "status": "SUCCESS"
    },
    "data": {
        "content": [...],
        "pageable": {...},
        "totalElements": 50,
        "totalPages": 5
    }
}
```

## Validation Rules

- **firstName**: Required, cannot be blank
- **lastName**: Required, cannot be blank
- **email**: Required, cannot be blank, must be valid email format, must be unique
- **address**: Optional

## Postman Collection

### 📦 Import Postman Collection

A comprehensive Postman collection is included for testing all APIs:

1. **Import the collection:**
   - `Inventory-Service-API.postman_collection.json`
   - `Inventory-Service-Local.postman_environment.json`

2. **Select the environment:**
   - Choose "Inventory Service - Local" from the environment dropdown

3. **Start testing:**
   - The collection includes 13 customer API requests + 3 management endpoints
   - Automated test scripts for validating responses
   - Pre-configured request bodies with sample data

### 📖 Documentation

- **[Postman Collection Guide](POSTMAN_COLLECTION_README.md)** - Detailed guide on using the Postman collection
- **[API Test Scenarios](API_TEST_SCENARIOS.md)** - Comprehensive test scenarios and edge cases

### Collection Features

- ✅ All CRUD operations
- ✅ Validation testing (invalid email, missing fields, etc.)
- ✅ Error scenarios (404, 409 conflicts)
- ✅ Pagination and sorting examples
- ✅ Automated test scripts
- ✅ Environment variables for dynamic data
- ✅ Health and management endpoints

## Project Structure

```
inventory-service/
├── src/main/java/com/myorg/is/
│   ├── InventoryServiceApplication.java
│   ├── advice/
│   │   └── ControllerAdvice.java          # Global exception handler
│   ├── config/
│   │   └── ManagementConfiguration.java   # Management endpoints config
│   ├── controller/
│   │   └── CustomerController.java        # REST endpoints
│   ├── exception/
│   │   └── StandardApiException.java      # Custom exception
│   ├── model/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   └── CustomerRequest.java   # Request DTO
│   │   │   └── response/
│   │   │       ├── CustomerResponse.java  # Response DTO
│   │   │       ├── StandardApiResponse.java
│   │   │       └── MetaData.java
│   │   └── entity/
│   │       └── Customer.java              # JPA Entity
│   ├── repository/
│   │   └── CustomerRepository.java        # Data access layer
│   ├── service/
│   │   ├── CustomerService.java           # Service interface
│   │   └── CustomerServiceImpl.java       # Business logic
│   └── util/
│       ├── enums/
│       │   └── Status.java
│       └── statics/
│           └── Constants.java
├── src/main/resources/
│   ├── application.yml                    # Application configuration
│   └── messages.properties                # Validation messages
├── Inventory-Service-API.postman_collection.json
├── Inventory-Service-Local.postman_environment.json
├── POSTMAN_COLLECTION_README.md
├── API_TEST_SCENARIOS.md
└── pom.xml
```

## Configuration

Configuration is managed in `src/main/resources/application.yml`:

- **Server Port**: 8080
- **Context Path**: /inventory-service
- **Database**: PostgreSQL on localhost:5435
- **Profile**: local (default)

## Error Handling

The application provides standardized error responses:

### 400 Bad Request
Invalid input or validation failure
```json
{
    "metaData": {
        "status": "ERROR"
    },
    "error": {
        "message": "Validation failed",
        "details": [...]
    }
}
```

### 404 Not Found
Customer not found
```json
{
    "metaData": {
        "status": "ERROR"
    },
    "error": {
        "message": "Customer not found for ID: 123"
    }
}
```

### 409 Conflict
Email already exists
```json
{
    "metaData": {
        "status": "ERROR"
    },
    "error": {
        "message": "Email already taken: john@example.com"
    }
}
```

## Testing

Run tests with:
```bash
mvn test
```

Or use the Postman collection for comprehensive API testing.

## Health Check

Check if the application is running:
```bash
curl http://localhost:8080/inventory-service/management/health
```

Expected response:
```json
{
    "status": "UP"
}
```

## Building for Production

```bash
mvn clean package -DskipTests
```

The JAR will be created in `target/inventory-service-0.0.1-SNAPSHOT.jar`

## Troubleshooting

### Application won't start
- Check if PostgreSQL is running on port 5435
- Verify database `inventory-service-db` exists
- Check database credentials in `application.yml`

### Port already in use
- Change the port in `application.yml`:
  ```yaml
  server:
    port: 8081
  ```

### Connection refused
- Ensure the application is running
- Check the correct URL: `http://localhost:8080/inventory-service`
- Verify context path configuration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the development team.

---

**Last Updated**: March 26, 2026  
**Version**: 0.0.1-SNAPSHOT

