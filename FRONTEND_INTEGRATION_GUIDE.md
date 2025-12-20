# Frontend Integration Guide

## Overview

The backend is a microservices architecture exposed via a central API Gateway.
**Base URL:** `http://localhost:8080`

All requests should be directed to the Base URL. The gateway handles routing to specific services based on the path.

## Global Configuration

### CORS
The API Gateway is configured to allow Cross-Origin Resource Sharing (CORS) from any origin (`*`).
Allowed Methods: `GET`, `POST`, `PUT`, `DELETE`, `PATCH`, `OPTIONS`.

### Authentication
Most endpoints require a JWT token for authentication.
**Header:** `Authorization: Bearer <your_token>`

## Services & Endpoints

### 1. User Service
**Base Path:** `/api/users`

| Method | Endpoint | Description | Auth Required |
|lz|---|---|---|
| POST | `/register` | Register a new user | No |
| POST | `/login` | Authenticate and get JWT | No |
| GET | `/profile/{id}` | Get user profile by ID | Yes |
| PUT | `/profile/{id}` | Update user profile | Yes |
| GET | `/email/{email}` | Get user by email | Yes |
| GET | `/wallet/{walletAddress}` | Get user by wallet address | Yes |
| GET | `/` | Get all users | Yes (Admin) |
| DELETE | `/{id}` | Delete user | Yes (Admin) |
| POST | `/verify-email/{userId}` | Verify user email | No |

**Register Payload:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "BUYER", // BUYER, SELLER, OWNER, TENANT
  "walletAddress": "0x123..." // Optional
}
```

### 2. Property Service
**Base Path:** `/api/properties`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/` | Create a property listing | Yes (Owner/Seller) |
| GET | `/{id}` | Get property details | No |
| GET | `/search` | Search properties | No |
| PUT | `/{id}` | Update property | Yes (Owner) |
| DELETE | `/{id}` | Delete property | Yes (Owner/Admin) |
| PATCH | `/{id}/status` | Update property status | Yes (Owner/Admin) |

**Search Parameters:**
- `city`, `country`, `minPrice`, `maxPrice`, `bedrooms`, `status`

**Create Property Payload:**
```json
{
  "title": "Luxury Apartment",
  "description": "Beautiful view...",
  "price": 1500.00,
  "address": "123 Main St",
  "city": "New York",
  "country": "USA",
  "type": "APARTMENT", // APARTMENT, HOUSE, VILLA
  "bedrooms": 2,
  "bathrooms": 1,
  "area": 85.5,
  "images": ["url1", "url2"]
}
```

### 3. Rental Service
**Base Path:** `/api/rentals`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/` | Create rental agreement | Yes (Tenant) |
| GET | `/{id}` | Get rental details | Yes |
| GET | `/tenant/{tenantId}` | Get rentals for tenant | Yes |
| GET | `/owner/{ownerId}` | Get rentals for owner | Yes |
| PATCH | `/{id}/status` | Update rental status | Yes |

**Create Rental Payload:**
```json
{
  "propertyId": 1,
  "tenantId": 1,
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "monthlyRent": 1500.00
}
```

### 4. Payment Service
**Base Path:** `/api/payments`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/` | Process a payment | Yes |
| GET | `/{id}` | Get payment details | Yes |
| GET | `/rental/{rentalId}` | Get payments for a rental | Yes |
| GET | `/payer/{payerId}` | Get payments by payer | Yes |

**Payment Payload:**
```json
{
  "rentalId": 1,
  "payerId": 1,
  "amount": 1500.00,
  "currency": "USD", // or ETH
  "method": "CREDIT_CARD", // CREDIT_CARD, CRYPTO
  "transactionHash": "0x..." // If CRYPTO
}
```

### 5. Blockchain Integration Service
**Base Path:** `/api/blockchain`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/transaction` | Execute smart contract transaction | Yes |
| GET | `/contract/{address}` | Get smart contract state | Yes |

**Smart Contract Request:**
```json
{
  "contractAddress": "0x...",
  "functionName": "payRent",
  "parameters": ["..."]
}
```

### 6. Notification Service
**Base Path:** `/api/notifications`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/` | Send a notification (System use) | Yes |
| GET | `/user/{userId}` | Get user notifications | Yes |

## Error Handling

Standard Error Response:
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

Common HTTP Codes:
- `200`: Success
- `201`: Created
- `400`: Bad Request (Validation failed)
- `401`: Unauthorized (Invalid/Missing Token)
- `403`: Forbidden (Insufficient permissions)
- `404`: Not Found
- `500`: Internal Server Error
