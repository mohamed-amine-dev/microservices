@echo off
REM Test script for Real Estate Microservices APIs

echo ========================================
echo Testing Real Estate Microservices APIs
echo ========================================

REM Set base URL
set BASE_URL=http://localhost:8080

echo.
echo ========================================
echo Test 1: Register Owner
echo ========================================
curl -X POST %BASE_URL%/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"owner@example.com\",\"password\":\"SecurePass123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"role\":\"OWNER\"}"
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 2: Register Tenant
echo ========================================
curl -X POST %BASE_URL%/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"tenant@example.com\",\"password\":\"SecurePass456\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"role\":\"TENANT\"}"
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 3: Login as Owner
echo ========================================
echo (Save the JWT token from the response)
curl -X POST %BASE_URL%/api/users/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"owner@example.com\",\"password\":\"SecurePass123\"}"
echo.
echo.
echo IMPORTANT: Copy the JWT token from above and paste it when prompted
set /p OWNER_TOKEN="Enter Owner JWT Token: "
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 4: Create Property Listing
echo ========================================
curl -X POST %BASE_URL%/api/properties ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %OWNER_TOKEN%" ^
  -d "{\"title\":\"Luxury Downtown Apartment\",\"description\":\"Beautiful 2-bedroom apartment\",\"price\":2000.00,\"address\":\"123 Main St\",\"city\":\"New York\",\"country\":\"USA\",\"type\":\"APARTMENT\",\"bedrooms\":2,\"bathrooms\":2,\"area\":95.5,\"images\":[]}"
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 5: Search Properties (No Auth)
echo ========================================
curl "%BASE_URL%/api/properties/search?city=New York"
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 6: Get Property Details
echo ========================================
curl %BASE_URL%/api/properties/1
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 7: Login as Tenant
echo ========================================
curl -X POST %BASE_URL%/api/users/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"tenant@example.com\",\"password\":\"SecurePass456\"}"
echo.
echo.
echo IMPORTANT: Copy the JWT token from above and paste it when prompted
set /p TENANT_TOKEN="Enter Tenant JWT Token: "
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 8: Create Rental Agreement
echo ========================================
curl -X POST %BASE_URL%/api/rentals ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TENANT_TOKEN%" ^
  -d "{\"propertyId\":1,\"tenantId\":2,\"startDate\":\"2025-01-01\",\"endDate\":\"2025-12-31\",\"monthlyRent\":2000.00}"
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 9: Process Payment
echo ========================================
curl -X POST %BASE_URL%/api/payments ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TENANT_TOKEN%" ^
  -d "{\"rentalId\":1,\"payerId\":2,\"amount\":2000.00,\"currency\":\"USD\",\"method\":\"CREDIT_CARD\"}"
echo.
timeout /t 2 /nobreak > nul

echo.
echo ========================================
echo Test 10: Check Service Health
echo ========================================
curl http://localhost:8761
echo.
echo.
echo ========================================
echo All tests completed!
echo ========================================
pause
