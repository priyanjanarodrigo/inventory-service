# Postman Collection for Inventory Service

This directory contains Postman collections and environments for testing the Inventory Service API.

## Files

- **Inventory-Service-API.postman_collection.json** - Complete API collection with all endpoints
- **Inventory-Service-Local.postman_environment.json** - Environment configuration for local testing

## Quick Start

### 1. Import into Postman

1. Open Postman
2. Click **Import** button (top-left)
3. Drag and drop both JSON files or click "Upload Files"
4. Select both files:
   - `Inventory-Service-API.postman_collection.json`
   - `Inventory-Service-Local.postman_environment.json`

### 2. Select Environment

1. In Postman, look for the environment dropdown (top-right)
2. Select **"Inventory Service - Local"**

### 3. Start Testing

1. Ensure your application is running on `http://localhost:8080`
2. Navigate through the collection folders
3. Run individual requests or use the Collection Runner

## Collection Structure

### Customer APIs (13 requests)

#### Create Operations
- **Create Customer** - Creates a customer with all fields including address
- **Create Customer - Without Address** - Creates a customer without optional address
- **Create Customer - Invalid Email** - Tests validation (expects 400)
- **Create Customer - Duplicate Email** - Tests conflict handling (expects 409)

#### Read Operations
- **Get Customer by ID** - Retrieves a specific customer
- **Get Customer by ID - Not Found** - Tests 404 error handling
- **Get All Customers** - Retrieves paginated list (page=0, size=10)
- **Get All Customers - Sorted** - Retrieves customers sorted by lastName and firstName

#### Update Operations
- **Update Customer** - Updates all customer fields including address
- **Update Customer - Not Found** - Tests update on non-existent customer (expects 404)

#### Delete Operations
- **Delete Customer** - Deletes a customer by ID (returns 204)
- **Delete Customer - Not Found** - Tests delete on non-existent customer (expects 404)

### Health & Management (3 requests)
- **Health Check** - Application health status
- **Metrics** - Available metrics
- **Application Info** - Application information

## Environment Variables

### Collection Variables
- `baseUrl` - Base URL for the API (default: `http://localhost:8080/inventory-service`)
- `customerId` - Dynamically set after creating a customer

The `customerId` variable is automatically populated when you run the "Create Customer" request, making it easy to chain requests together.

## Running the Tests

### Individual Request
Simply select a request and click **Send**

### Entire Collection
1. Click on the collection name
2. Click **Run** button
3. Select requests to run
4. Configure iterations and delay if needed
5. Click **Run Inventory Service API**

### Test Scripts
Each request includes test scripts that validate:
- HTTP status codes
- Response structure
- Data integrity
- Business logic validation

## API Endpoints Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/customers` | Create a new customer |
| GET | `/api/v1/customers/{id}` | Get customer by ID |
| GET | `/api/v1/customers` | Get all customers (paginated) |
| PUT | `/api/v1/customers/{id}` | Update a customer |
| DELETE | `/api/v1/customers/{id}` | Delete a customer |
| GET | `/management/health` | Health check |
| GET | `/management/metrics` | Application metrics |
| GET | `/management/info` | Application info |

## Request & Response Examples

### Create Customer Request
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main Street, New York, NY 10001"
}
```

### Success Response
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

## Pagination Parameters

For the "Get All Customers" endpoint, you can use:
- `page` - Page number (0-indexed)
- `size` - Items per page
- `sort` - Sort field and direction (e.g., `lastName,asc`)

Example: `?page=0&size=20&sort=lastName,asc&sort=firstName,asc`

## Testing Workflow

### Recommended Test Sequence:
1. **Health Check** - Verify the service is running
2. **Create Customer** - Creates a customer and stores ID
3. **Get Customer by ID** - Uses stored ID from step 2
4. **Get All Customers** - View all customers
5. **Update Customer** - Update the created customer
6. **Get Customer by ID** - Verify the update
7. **Delete Customer** - Clean up test data
8. **Get Customer by ID - Not Found** - Verify deletion

## Error Testing

The collection includes requests that test error scenarios:
- **400 Bad Request** - Invalid email format, null ID, etc.
- **404 Not Found** - Non-existent customer
- **409 Conflict** - Duplicate email, email already associated with customer

## Tips

1. **Auto-generated ID**: The "Create Customer" request automatically saves the customer ID to the environment variable `customerId`
2. **Test Scripts**: Check the "Tests" tab of each request to see validation logic
3. **Pre-request Scripts**: Can be added for dynamic data generation
4. **Console**: Use Postman Console (View → Show Postman Console) to debug requests

## Troubleshooting

### Connection Refused
- Ensure the application is running: `mvn spring-boot:run` or `java -jar target/inventory-service-0.0.1-SNAPSHOT.jar`
- Verify the port is 8080: Check `src/main/resources/application.yml`

### 404 on All Endpoints
- Check that context path is correct: `/inventory-service`
- Verify baseUrl in environment: `http://localhost:8080/inventory-service`

### Database Errors
- Ensure PostgreSQL is running on port 5435
- Verify database `inventory-service-db` exists
- Check credentials: username=`postgres`, password=`postgres@16`

## Additional Resources

- [Postman Documentation](https://learning.postman.com/docs/getting-started/introduction/)
- [Spring Boot Actuator Endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

---

**Last Updated**: March 26, 2026
**API Version**: v1
**Collection Version**: 1.0.0

