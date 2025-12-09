# Real Estate Rental Platform - Frontend Integration Guide

## 📋 For Frontend Developers

This document provides everything you need to integrate with the Real Estate Rental Platform backend.

---

## 🚀 Quick Start

### Prerequisites
Your backend team needs to:
1. Install Maven
2. Build the project: `mvn clean install -DskipTests`
3. Start services: `docker-compose up -d`
4. Verify all services are running

### Base URLs

**Production/Development:**
- API Gateway (all requests): `http://localhost:8080`
- Eureka Dashboard: `http://localhost:8761`

**Individual Services (if needed):**
- User Service: `http://localhost:8081`
- Property Service: `http://localhost:8082`
- Rental Service: `http://localhost:8083`
- Payment Service: `http://localhost:8084`
- Blockchain Service: `http://localhost:8085`
- Notification Service: `http://localhost:8086`

---

## 🔐 Authentication Flow

### 1. Register a New User

**Endpoint:** `POST /api/users/register`

**Request:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "OWNER",  // or "TENANT"
  "walletAddress": "0x1234...abcd"  // optional
}
```

**Response (201 Created):**
```json
{
  "status": 200,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": 1,
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "role": "OWNER",
      "walletAddress": "0x1234...abcd",
      "emailVerified": false,
      "active": true,
      "createdAt": "2025-12-09T18:00:00",
      "updatedAt": "2025-12-09T18:00:00"
    }
  },
  "timestamp": "2025-12-09T18:00:00"
}
```

### 2. Login

**Endpoint:** `POST /api/users/login`

**Request:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Response (200 OK):**
```json
{
  "status": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": { /* user details */ }
  }
}
```

### 3. Using the JWT Token

Include the token in **all subsequent requests**:

```javascript
headers: {
  'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...',
  'Content-Type': 'application/json'
}
```

---

## 🏠 Property Management API

### List All Properties / Search

**Endpoint:** `GET /api/properties/search`

**Query Parameters (all optional):**
- `city`: string
- `country`: string
- `minPrice`: number
- `maxPrice`: number
- `bedrooms`: number
- `status`: "AVAILABLE" | "RENTED" | "MAINTENANCE"

**Example:**
```
GET /api/properties/search?city=New York&minPrice=1000&maxPrice=3000&bedrooms=2
```

**Response:**
```json
{
  "status": 200,
  "data": [
    {
      "id": 1,
      "title": "Modern Apartment",
      "description": "2BR apartment in city center",
      "address": "123 Main St",
      "city": "New York",
      "country": "USA",
      "latitude": 40.7128,
      "longitude": -74.0060,
      "pricePerMonth": 2000,
      "depositAmount": 4000,
      "bedroomCount": 2,
      "bathroomCount": 1,
      "squareMeters": 80,
      "propertyType": "APARTMENT",
      "status": "AVAILABLE",
      "ownerId": 1,
      "imageUrls": [
        "http://localhost:8082/uploads/property1-img1.jpg",
        "http://localhost:8082/uploads/property1-img2.jpg"
      ],
      "createdAt": "2025-12-09T10:00:00",
      "updatedAt": "2025-12-09T10:00:00"
    }
  ]
}
```

### Get Single Property

**Endpoint:** `GET /api/properties/{id}`

**Response:** Same as single property object above

### Create Property (Owner Only)

**Endpoint:** `POST /api/properties`

**Requires:** JWT token from user with role "OWNER"

**Request:**
```json
{
  "title": "Modern Apartment",
  "description": "Spacious 2BR apartment",
  "address": "123 Main St, Apt 4B",
  "city": "New York",
  "country": "USA",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "pricePerMonth": 2000,
  "depositAmount": 4000,
  "bedroomCount": 2,
  "bathroomCount": 1,
  "squareMeters": 80,
  "propertyType": "APARTMENT",
  "ownerId": 1
}
```

**Response (201 Created):** Property object

### Update Property

**Endpoint:** `PUT /api/properties/{id}`

**Requires:** JWT token (owner of property)

**Request:** Same as create

### Delete Property

**Endpoint:** `DELETE /api/properties/{id}`

**Requires:** JWT token (owner of property)

---

## 🏘️ Property Types

```typescript
enum PropertyType {
  APARTMENT = "APARTMENT",
  HOUSE = "HOUSE",
  VILLA = "VILLA",
  STUDIO = "STUDIO",
  CONDO = "CONDO",
  TOWNHOUSE = "TOWNHOUSE",
  DUPLEX = "DUPLEX"
}
```

## 📊 Property Status

```typescript
enum PropertyStatus {
  AVAILABLE = "AVAILABLE",
  RENTED = "RENTED",
  MAINTENANCE = "MAINTENANCE"
}
```

---

## 🏢 Rental Management API

### Create Rental Request (Tenant)

**Endpoint:** `POST /api/rentals/request`

**Requires:** JWT token (tenant)

**Request:**
```json
{
  "propertyId": 1,
  "message": "I'm interested in renting this property",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31"
}
```

### Get User's Rentals

**Tenant Rentals:** `GET /api/rentals/tenant/{tenantId}`

**Owner Rentals:** `GET /api/rentals/owner/{ownerId}`

**Property Rentals:** `GET /api/rentals/property/{propertyId}`

---

## 💳 Payment API

### Get Payment History

**Endpoint:** `GET /api/payments/rental/{rentalId}`

**Response:**
```json
{
  "status": 200,
  "data": [
    {
      "id": 1,
      "rentalId": 1,
      "paymentType": "DEPOSIT",
      "amount": 4000,
      "transactionHash": "0xabc123...",
      "blockchainStatus": "CONFIRMED",
      "paymentDate": "2025-01-01T10:00:00",
      "createdAt": "2025-01-01T10:00:00"
    }
  ]
}
```

### Payment Types
- `DEPOSIT` - Initial deposit
- `MONTHLY_RENT` - Monthly rent payment
- `REFUND` - Deposit refund

---

## 🔗 User Roles

```typescript
enum UserRole {
  OWNER = "OWNER",     // Can create/manage properties
  TENANT = "TENANT",   // Can search and rent properties
  ADMIN = "ADMIN"      // Full access
}
```

---

## 🌐 CORS Configuration

**CORS is already configured** in the API Gateway to accept requests from any origin in development.

For production, update `api-gateway/src/main/resources/application.yml`:
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins: "https://yourfrontend.com"
```

---

## 📱 Example Frontend Integration (React/Vue/Angular)

### Authentication Service

```javascript
// auth.service.js
const API_URL = 'http://localhost:8080';

export const authService = {
  async register(userData) {
    const response = await fetch(`${API_URL}/api/users/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData)
    });
    const data = await response.json();
    if (data.data.token) {
      localStorage.setItem('token', data.data.token);
      localStorage.setItem('user', JSON.stringify(data.data.user));
    }
    return data;
  },

  async login(email, password) {
    const response = await fetch(`${API_URL}/api/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    const data = await response.json();
    if (data.data.token) {
      localStorage.setItem('token', data.data.token);
      localStorage.setItem('user', JSON.stringify(data.data.user));
    }
    return data;
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getToken() {
    return localStorage.getItem('token');
  },

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }
};
```

### Property Service

```javascript
// property.service.js
const API_URL = 'http://localhost:8080';

export const propertyService = {
  async searchProperties(filters) {
    const params = new URLSearchParams(filters);
    const token = localStorage.getItem('token');
    
    const response = await fetch(`${API_URL}/api/properties/search?${params}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    return await response.json();
  },

  async createProperty(propertyData) {
    const token = localStorage.getItem('token');
    const response = await fetch(`${API_URL}/api/properties`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(propertyData)
    });
    return await response.json();
  }
};
```

---

## 🧪 Testing the API

### Using Swagger UI
Once the backend is running, visit:
- User Service: http://localhost:8081/swagger-ui.html
- Property Service: http://localhost:8082/swagger-ui.html
- Rental Service: http://localhost:8083/swagger-ui.html
- Payment Service: http://localhost:8084/swagger-ui.html

### Using Postman
1. Import the following base URL: `http://localhost:8080`
2. Create requests for each endpoint
3. Add Authorization header with Bearer token for protected endpoints

### Using curl

**Register:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","firstName":"John","lastName":"Doe","role":"OWNER"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

**Search Properties:**
```bash
curl "http://localhost:8080/api/properties/search?city=New%20York"
```

---

## ⚠️ Important Notes

### Before Frontend Development:

1. **Backend Must Be Running:**
   ```bash
   docker-compose up -d
   ```
   Wait 1-2 minutes for all services to start

2. **Verify Services:**
   - Check Eureka: http://localhost:8761
   - Should see 7 services registered

3. **Test Authentication:**
   - Register a test user
   - Verify you receive a JWT token

### Error Handling

**Standard Error Response:**
```json
{
  "errorCode": "RESOURCE_NOT_FOUND",
  "message": "Property not found with id: '99'",
  "details": null,
  "timestamp": "2025-12-09T18:00:00",
  "path": "/api/properties/99",
  "status": 404
}
```

**Validation Error Response:**
```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": [
    "email: Invalid email format",
    "password: Password must be at least 6 characters"
  ],
  "timestamp": "2025-12-09T18:00:00",
  "path": "/api/users/register",
  "status": 400
}
```

---

## 📞 Communication Checklist

Share with frontend team:
- ✅ GitHub repository URL: https://github.com/mohamed-amine-dev/real-estate-rental-backend-part
- ✅ This integration guide
- ✅ Base URL: http://localhost:8080
- ✅ Swagger documentation URLs
- ✅ Expected response formats
- ✅ Authentication flow

---

## 🚨 Common Issues

**"ERR_CONNECTION_REFUSED"**
- Backend is not running
- Run: `docker-compose up -d`

**"401 Unauthorized"**
- Missing or invalid JWT token
- Token expired (24 hours)
- Need to login again

**"CORS Error"**
- Should not happen (already configured)
- If it does, check API Gateway CORS settings

---

## 📚 Additional Resources

- Full README: See `README.md` in repository
- Testing Guide: See `TESTING_GUIDE.md`
- API Documentation: Swagger UI at service URLs

---

**Questions?** Contact the backend team!
