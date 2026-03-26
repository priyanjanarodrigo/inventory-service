# API Test Scenarios for Inventory Service

This document outlines comprehensive test scenarios for the Inventory Service Customer APIs.

## Test Scenario 1: Happy Path - Complete Customer Lifecycle

### Objective
Test the complete CRUD lifecycle of a customer with all fields populated.

### Steps
1. **Create Customer**
   - **Request**: POST `/api/v1/customers`
   - **Body**:
     ```json
     {
       "firstName": "Alice",
       "lastName": "Johnson",
       "email": "alice.johnson@example.com",
       "address": "456 Oak Avenue, Boston, MA 02101"
     }
     ```
   - **Expected**: 201 Created, Location header present, customer ID returned
   - **Save**: Customer ID for subsequent requests

2. **Retrieve Created Customer**
   - **Request**: GET `/api/v1/customers/{id}`
   - **Expected**: 200 OK, all fields match creation data
   - **Verify**: firstName, lastName, email, address are correct

3. **Update Customer**
   - **Request**: PUT `/api/v1/customers/{id}`
   - **Body**:
     ```json
     {
       "firstName": "Alice",
       "lastName": "Johnson-Smith",
       "email": "alice.johnson.smith@example.com",
       "address": "789 Maple Street, Boston, MA 02102"
     }
     ```
   - **Expected**: 200 OK, updated fields returned

4. **Verify Update**
   - **Request**: GET `/api/v1/customers/{id}`
   - **Expected**: 200 OK, lastName and email and address are updated

5. **Delete Customer**
   - **Request**: DELETE `/api/v1/customers/{id}`
   - **Expected**: 204 No Content, empty response body

6. **Verify Deletion**
   - **Request**: GET `/api/v1/customers/{id}`
   - **Expected**: 404 Not Found

---

## Test Scenario 2: Optional Fields - Customer Without Address

### Objective
Verify that the address field is truly optional and the system handles null values correctly.

### Steps
1. **Create Customer Without Address**
   - **Request**: POST `/api/v1/customers`
   - **Body**:
     ```json
     {
       "firstName": "Bob",
       "lastName": "Williams",
       "email": "bob.williams@example.com"
     }
     ```
   - **Expected**: 201 Created, address field is null or not present

2. **Retrieve Customer**
   - **Request**: GET `/api/v1/customers/{id}`
   - **Expected**: 200 OK, address is null
   - **Verify**: Other fields are populated correctly

3. **Add Address via Update**
   - **Request**: PUT `/api/v1/customers/{id}`
   - **Body**:
     ```json
     {
       "firstName": "Bob",
       "lastName": "Williams",
       "email": "bob.williams@example.com",
       "address": "321 Pine Road, Chicago, IL 60601"
     }
     ```
   - **Expected**: 200 OK, address is now populated

4. **Remove Address via Update**
   - **Request**: PUT `/api/v1/customers/{id}`
   - **Body**:
     ```json
     {
       "firstName": "Bob",
       "lastName": "Williams",
       "email": "bob.williams@example.com",
       "address": null
     }
     ```
   - **Expected**: 200 OK, address is null again

---

## Test Scenario 3: Validation Testing

### Objective
Test all validation rules and error handling.

### Test 3.1: Missing Required Field - First Name
```json
{
  "lastName": "Test",
  "email": "test@example.com"
}
```
**Expected**: 400 Bad Request, error message about firstName

### Test 3.2: Missing Required Field - Last Name
```json
{
  "firstName": "Test",
  "email": "test@example.com"
}
```
**Expected**: 400 Bad Request, error message about lastName

### Test 3.3: Missing Required Field - Email
```json
{
  "firstName": "Test",
  "lastName": "User"
}
```
**Expected**: 400 Bad Request, error message about email

### Test 3.4: Invalid Email Format
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "not-an-email"
}
```
**Expected**: 400 Bad Request, error message about email format

### Test 3.5: Empty String Values
```json
{
  "firstName": "",
  "lastName": "",
  "email": ""
}
```
**Expected**: 400 Bad Request, error messages for blank fields

### Test 3.6: Null Values for Required Fields
```json
{
  "firstName": null,
  "lastName": null,
  "email": null
}
```
**Expected**: 400 Bad Request, error messages for null fields

---

## Test Scenario 4: Duplicate Email Handling

### Objective
Test the unique constraint on email addresses.

### Steps
1. **Create First Customer**
   - **Email**: "unique.test@example.com"
   - **Expected**: 201 Created

2. **Attempt to Create Duplicate**
   - **Email**: "unique.test@example.com" (same email)
   - **Expected**: 409 Conflict, error message about email already taken

3. **Update with Same Email**
   - **Request**: PUT `/api/v1/customers/{id}` (update the first customer)
   - **Body**: Same email that the customer already has
   - **Expected**: 409 Conflict, error message about email already associated with customer

4. **Update with Different Customer's Email**
   - Create second customer with email "second@example.com"
   - Try to update first customer to use "second@example.com"
   - **Expected**: 409 Conflict, error message about email already taken

---

## Test Scenario 5: Pagination and Sorting

### Objective
Test pagination and sorting functionality for the customer list.

### Test 5.1: Default Pagination
- **Request**: GET `/api/v1/customers`
- **Expected**: First page of results, default page size
- **Verify**: Response contains `content`, `pageable`, `totalElements`, `totalPages`

### Test 5.2: Custom Page Size
- **Request**: GET `/api/v1/customers?page=0&size=5`
- **Expected**: Maximum 5 customers in content array

### Test 5.3: Second Page
- **Request**: GET `/api/v1/customers?page=1&size=5`
- **Expected**: Second page of results (skip first 5)

### Test 5.4: Sort by Last Name Ascending
- **Request**: GET `/api/v1/customers?sort=lastName,asc`
- **Expected**: Customers sorted alphabetically by last name

### Test 5.5: Sort by Last Name Descending
- **Request**: GET `/api/v1/customers?sort=lastName,desc`
- **Expected**: Customers sorted reverse alphabetically by last name

### Test 5.6: Multi-field Sorting
- **Request**: GET `/api/v1/customers?sort=lastName,asc&sort=firstName,asc`
- **Expected**: Customers sorted by last name, then first name

### Test 5.7: Large Page Size
- **Request**: GET `/api/v1/customers?page=0&size=1000`
- **Expected**: All customers (up to 1000) in single page

---

## Test Scenario 6: Error Handling

### Test 6.1: Invalid Customer ID
- **Request**: GET `/api/v1/customers/abc`
- **Expected**: 400 Bad Request (invalid ID format)

### Test 6.2: Negative Customer ID
- **Request**: GET `/api/v1/customers/-1`
- **Expected**: 400 Bad Request (ID must be greater than zero)

### Test 6.3: Zero Customer ID
- **Request**: GET `/api/v1/customers/0`
- **Expected**: 400 Bad Request (ID must be greater than zero)

### Test 6.4: Non-existent Customer ID
- **Request**: GET `/api/v1/customers/999999`
- **Expected**: 404 Not Found

### Test 6.5: Update Non-existent Customer
- **Request**: PUT `/api/v1/customers/999999`
- **Expected**: 404 Not Found

### Test 6.6: Delete Non-existent Customer
- **Request**: DELETE `/api/v1/customers/999999`
- **Expected**: 404 Not Found

---

## Test Scenario 7: Edge Cases

### Test 7.1: Very Long Address
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "long.address@example.com",
  "address": "A very long address that contains many characters to test if there are any length limitations on the address field. This address includes street number, street name, apartment number, city, state, ZIP code, and country information all in one very long string that might exceed typical length expectations."
}
```
**Expected**: Depends on database column size - either 201 Created or 400 Bad Request

### Test 7.2: Special Characters in Name
```json
{
  "firstName": "François",
  "lastName": "O'Brien-Smith",
  "email": "special.chars@example.com",
  "address": "123 Müller Straße"
}
```
**Expected**: 201 Created, special characters preserved

### Test 7.3: Email with Plus Sign
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "user+tag@example.com"
}
```
**Expected**: 201 Created (valid email format)

### Test 7.4: International Email Domain
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "user@例え.jp"
}
```
**Expected**: 201 Created or validation error (depending on validation rules)

---

## Test Scenario 8: Concurrent Operations

### Objective
Test system behavior under concurrent requests.

### Test 8.1: Simultaneous Customer Creation
- Send 10 POST requests simultaneously with different emails
- **Expected**: All should succeed with 201 Created

### Test 8.2: Race Condition - Same Email
- Send 2 POST requests simultaneously with the same email
- **Expected**: One succeeds (201), one fails (409)

### Test 8.3: Update While Reading
- Start a GET request for a customer
- Simultaneously send a PUT request to update the same customer
- **Expected**: Both complete successfully, GET returns old or new data

---

## Test Scenario 9: Health and Management Endpoints

### Test 9.1: Health Check
- **Request**: GET `/management/health`
- **Expected**: 200 OK, status: "UP"

### Test 9.2: Application Metrics
- **Request**: GET `/management/metrics`
- **Expected**: 200 OK, list of available metrics

### Test 9.3: Application Info
- **Request**: GET `/management/info`
- **Expected**: 200 OK, application information

---

## Test Scenario 10: Performance Testing

### Objective
Test API performance under load.

### Test 10.1: Create 100 Customers
- Create 100 customers with unique emails
- **Measure**: Average response time, success rate
- **Expected**: All succeed, reasonable response time (<500ms per request)

### Test 10.2: Retrieve All Customers (Large Dataset)
- After creating 100+ customers
- **Request**: GET `/api/v1/customers?page=0&size=100`
- **Measure**: Response time, payload size
- **Expected**: Returns within reasonable time (<1s)

### Test 10.3: Pagination Performance
- Test different page sizes (10, 50, 100, 500)
- **Measure**: Response time for each page size
- **Expected**: Linear or sub-linear increase in response time

---

## Test Data Sets

### Valid Email Formats
- simple@example.com
- user.name@example.com
- user+tag@example.co.uk
- user_name@sub.example.com
- 123@example.com

### Invalid Email Formats
- plaintext
- @example.com
- user@
- user @example.com (space)
- user@example (no TLD)

### Sample Addresses
- "123 Main St, New York, NY 10001"
- "Apt 4B, 456 Oak Avenue, Boston, MA 02101"
- "789 Pine Road, Suite 200, Chicago, IL 60601"
- null (optional field)
- "" (empty string)

---

## Expected Response Codes Summary

| Operation | Success | Not Found | Validation Error | Conflict |
|-----------|---------|-----------|------------------|----------|
| POST /customers | 201 | N/A | 400 | 409 |
| GET /customers/{id} | 200 | 404 | 400 | N/A |
| GET /customers | 200 | N/A | 400 | N/A |
| PUT /customers/{id} | 200 | 404 | 400 | 409 |
| DELETE /customers/{id} | 204 | 404 | 400 | N/A |

---

**Note**: All test scenarios should be executed with proper cleanup to avoid data pollution in the test database.

