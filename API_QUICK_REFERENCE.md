# API Quick Reference Card

## Base URL
```
http://localhost:8080/inventory-service
```

## Quick cURL Commands

### 1. Create Customer
```bash
curl -X POST http://localhost:8080/inventory-service/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St, New York, NY 10001"
  }'
```

### 2. Get Customer by ID
```bash
curl -X GET http://localhost:8080/inventory-service/api/v1/customers/1
```

### 3. Get All Customers (Paginated)
```bash
curl -X GET "http://localhost:8080/inventory-service/api/v1/customers?page=0&size=10&sort=lastName,asc"
```

### 4. Update Customer
```bash
curl -X PUT http://localhost:8080/inventory-service/api/v1/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe Updated",
    "email": "john.updated@example.com",
    "address": "456 New Address"
  }'
```

### 5. Delete Customer
```bash
curl -X DELETE http://localhost:8080/inventory-service/api/v1/customers/1
```

### 6. Health Check
```bash
curl -X GET http://localhost:8080/inventory-service/management/health
```

---

## PowerShell Commands

### 1. Create Customer
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/inventory-service/api/v1/customers" `
  -Method POST `
  -ContentType "application/json" `
  -Body (@{
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
    address = "123 Main St, New York, NY 10001"
  } | ConvertTo-Json)
```

### 2. Get Customer by ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/inventory-service/api/v1/customers/1" `
  -Method GET
```

### 3. Get All Customers
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/inventory-service/api/v1/customers?page=0&size=10" `
  -Method GET
```

### 4. Update Customer
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/inventory-service/api/v1/customers/1" `
  -Method PUT `
  -ContentType "application/json" `
  -Body (@{
    firstName = "John"
    lastName = "Doe Updated"
    email = "john.updated@example.com"
    address = "456 New Address"
  } | ConvertTo-Json)
```

### 5. Delete Customer
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/inventory-service/api/v1/customers/1" `
  -Method DELETE
```

### 6. Health Check
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/inventory-service/management/health" `
  -Method GET
```

---

## Response Status Codes

| Code | Meaning | Occurs When |
|------|---------|-------------|
| 200 | OK | GET, PUT successful |
| 201 | Created | POST successful |
| 204 | No Content | DELETE successful |
| 400 | Bad Request | Validation failed, invalid ID |
| 404 | Not Found | Customer doesn't exist |
| 409 | Conflict | Duplicate email |
| 500 | Server Error | Internal server error |

---

## Common Query Parameters

### Pagination
- `page` - Page number (0-indexed, default: 0)
- `size` - Items per page (default: 20)

### Sorting
- `sort` - Field and direction (e.g., `lastName,asc`)
- Multiple sorts: `sort=lastName,asc&sort=firstName,desc`

**Examples:**
```
/api/v1/customers?page=0&size=10
/api/v1/customers?sort=lastName,asc
/api/v1/customers?page=1&size=20&sort=email,desc
```

---

## Sample Request Bodies

### Minimal Customer (Without Address)
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com"
}
```

### Complete Customer (With Address)
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "address": "123 Main Street, New York, NY 10001"
}
```

---

## Sample Response Format

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

### Paginated Response
```json
{
  "metaData": {
    "status": "SUCCESS"
  },
  "data": {
    "content": [
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "address": "123 Main St"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false
  }
}
```

### Error Response
```json
{
  "metaData": {
    "status": "ERROR"
  },
  "error": {
    "message": "Customer not found for ID: 123",
    "timestamp": "2026-03-26T12:00:00",
    "path": "/api/v1/customers/123"
  }
}
```

---

## Field Constraints

| Field | Required | Validation | Unique |
|-------|----------|------------|--------|
| firstName | ✅ Yes | Not blank | ❌ No |
| lastName | ✅ Yes | Not blank | ❌ No |
| email | ✅ Yes | Valid email format | ✅ Yes |
| address | ❌ No | None | ❌ No |

---

## Testing Workflow

1. **Health Check** → Verify service is running
2. **Create Customer** → Save returned ID
3. **Get Customer** → Verify creation
4. **Get All Customers** → Check pagination
5. **Update Customer** → Modify data
6. **Get Customer** → Verify update
7. **Delete Customer** → Remove data
8. **Get Customer** → Confirm deletion (404)

---

## Troubleshooting

### "Connection refused"
✅ Check if application is running: `mvn spring-boot:run`

### "404 Not Found" on all endpoints
✅ Verify base URL includes context path: `/inventory-service`

### "409 Conflict" on create
✅ Email already exists, use a different email

### "400 Bad Request"
✅ Check request body matches required fields and format

---

**Tip**: Use Postman collection for automated testing with pre-configured requests and test scripts!

📦 **Postman Collection**: `Inventory-Service-API.postman_collection.json`  
📖 **Full Documentation**: See `POSTMAN_COLLECTION_README.md`

