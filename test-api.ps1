# Quick API Test Script for Inventory Service
# This PowerShell script performs basic smoke tests on the API

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "  Inventory Service API - Quick Smoke Test" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/inventory-service"
$testEmail = "test.user.$(Get-Random)@example.com"
$customerId = $null

# Function to display test results
function Show-TestResult {
    param(
        [string]$TestName,
        [bool]$Success,
        [string]$Message = ""
    )

    if ($Success) {
        Write-Host "✓ $TestName" -ForegroundColor Green
    } else {
        Write-Host "✗ $TestName" -ForegroundColor Red
    }

    if ($Message) {
        Write-Host "  $Message" -ForegroundColor Gray
    }
}

# Test 1: Health Check
Write-Host "Test 1: Health Check" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/management/health" -Method GET
    if ($response.status -eq "UP") {
        Show-TestResult "Health Check" $true "Application is UP"
    } else {
        Show-TestResult "Health Check" $false "Application status: $($response.status)"
    }
} catch {
    Show-TestResult "Health Check" $false "Failed to connect. Is the application running?"
    Write-Host ""
    Write-Host "Please start the application first:" -ForegroundColor Yellow
    Write-Host "  mvn spring-boot:run" -ForegroundColor White
    exit 1
}

Write-Host ""

# Test 2: Create Customer
Write-Host "Test 2: Create Customer" -ForegroundColor Yellow
try {
    $createBody = @{
        firstName = "Test"
        lastName = "User"
        email = $testEmail
        address = "123 Test Street, Test City, TC 12345"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/api/v1/customers" `
        -Method POST `
        -ContentType "application/json" `
        -Body $createBody

    if ($response.data.id) {
        $customerId = $response.data.id
        Show-TestResult "Create Customer" $true "Customer ID: $customerId"
    } else {
        Show-TestResult "Create Customer" $false "No customer ID returned"
    }
} catch {
    Show-TestResult "Create Customer" $false $_.Exception.Message
}

Write-Host ""

# Test 3: Get Customer by ID
if ($customerId) {
    Write-Host "Test 3: Get Customer by ID" -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/v1/customers/$customerId" -Method GET

        if ($response.data.id -eq $customerId -and $response.data.email -eq $testEmail) {
            Show-TestResult "Get Customer by ID" $true "Retrieved customer successfully"
        } else {
            Show-TestResult "Get Customer by ID" $false "Data mismatch"
        }
    } catch {
        Show-TestResult "Get Customer by ID" $false $_.Exception.Message
    }

    Write-Host ""
}

# Test 4: Get All Customers
Write-Host "Test 4: Get All Customers (Paginated)" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/v1/customers?page=0&size=10" -Method GET

    if ($response.data.content -and $response.data.totalElements -ge 0) {
        Show-TestResult "Get All Customers" $true "Total customers: $($response.data.totalElements)"
    } else {
        Show-TestResult "Get All Customers" $false "Invalid response structure"
    }
} catch {
    Show-TestResult "Get All Customers" $false $_.Exception.Message
}

Write-Host ""

# Test 5: Update Customer
if ($customerId) {
    Write-Host "Test 5: Update Customer" -ForegroundColor Yellow
    try {
        $updateBody = @{
            firstName = "Test Updated"
            lastName = "User Updated"
            email = "updated.$testEmail"
            address = "456 Updated Street, Updated City, UC 67890"
        } | ConvertTo-Json

        $response = Invoke-RestMethod -Uri "$baseUrl/api/v1/customers/$customerId" `
            -Method PUT `
            -ContentType "application/json" `
            -Body $updateBody

        if ($response.data.firstName -eq "Test Updated") {
            Show-TestResult "Update Customer" $true "Customer updated successfully"
        } else {
            Show-TestResult "Update Customer" $false "Update did not apply"
        }
    } catch {
        Show-TestResult "Update Customer" $false $_.Exception.Message
    }

    Write-Host ""
}

# Test 6: Delete Customer
if ($customerId) {
    Write-Host "Test 6: Delete Customer" -ForegroundColor Yellow
    try {
        Invoke-RestMethod -Uri "$baseUrl/api/v1/customers/$customerId" -Method DELETE
        Show-TestResult "Delete Customer" $true "Customer deleted successfully"
    } catch {
        Show-TestResult "Delete Customer" $false $_.Exception.Message
    }

    Write-Host ""
}

# Test 7: Verify Deletion (Should get 404)
if ($customerId) {
    Write-Host "Test 7: Verify Deletion (Expect 404)" -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/v1/customers/$customerId" -Method GET
        Show-TestResult "Verify Deletion" $false "Customer still exists (should be deleted)"
    } catch {
        if ($_.Exception.Response.StatusCode.value__ -eq 404) {
            Show-TestResult "Verify Deletion" $true "Customer not found (as expected)"
        } else {
            Show-TestResult "Verify Deletion" $false "Unexpected error: $($_.Exception.Message)"
        }
    }

    Write-Host ""
}

# Test 8: Validation Test (Invalid Email)
Write-Host "Test 8: Validation Test (Invalid Email)" -ForegroundColor Yellow
try {
    $invalidBody = @{
        firstName = "Invalid"
        lastName = "Test"
        email = "not-an-email"
        address = "Test Address"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/api/v1/customers" `
        -Method POST `
        -ContentType "application/json" `
        -Body $invalidBody

    Show-TestResult "Validation Test" $false "Should have rejected invalid email"
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 400) {
        Show-TestResult "Validation Test" $true "Invalid email rejected (as expected)"
    } else {
        Show-TestResult "Validation Test" $false "Unexpected error: $($_.Exception.Message)"
    }
}

Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "  Smoke Test Complete!" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "For comprehensive testing, import the Postman collection:" -ForegroundColor White
Write-Host "  • Inventory-Service-API.postman_collection.json" -ForegroundColor Gray
Write-Host "  • Inventory-Service-Local.postman_environment.json" -ForegroundColor Gray
Write-Host ""
Write-Host "See POSTMAN_COLLECTION_README.md for details." -ForegroundColor White

