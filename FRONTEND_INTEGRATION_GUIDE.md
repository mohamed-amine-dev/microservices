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

### Response Format
All API responses follow a standard wrapper format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... } // The actual payload
}
```

## Services & Endpoints

### 1. User Service
**Base Path:** `/api/users`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/register` | Register a new user | No |
| POST | `/login` | Authenticate and get JWT | No |
| GET | `/profile/{id}` | Get user profile by ID | Yes |
| PUT | `/profile/{id}` | Update user profile | Yes |
| GET | `/email/{email}` | Get user by email | Yes |
| GET | `/wallet/{walletAddress}` | Get user by wallet address | Yes |
| GET | `/` | Get all users | Yes (Admin) |
| DELETE | `/{id}` | Delete user | Yes (Admin) |
| POST | `/verify-email/{userId}` | Verify user email | No |

#### Payloads

**Register Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123", // Min 6 chars
  "firstName": "John",
  "lastName": "Doe",
  "role": "BUYER", // Options: ADMIN, BUYER, SELLER, OWNER, TENANT
  "walletAddress": "0x123..." // Optional
}
```

**Login Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123"
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

#### Search Parameters (Query Params)
`city`, `country`, `minPrice`, `maxPrice`, `bedrooms`, `status`

#### Payloads

**Create/Update Property Request:**
```json
{
  "title": "Luxury Apartment",
  "description": "Beautiful view...",
  "address": "123 Main St",
  "city": "New York",
  "country": "USA",
  "latitude": 40.7128, // Optional
  "longitude": -74.0060, // Optional
  "pricePerMonth": 1500.00,
  "depositAmount": 3000.00,
  "bedroomCount": 2,
  "bathroomCount": 1,
  "squareMeters": 85.5,
  "propertyType": "APARTMENT", // APARTMENT, HOUSE, VILLA, COMMERCIAL
  "ownerId": 1
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

#### Payloads

**Create Rental Request:**
```json
{
  "propertyId": 1,
  "tenantId": 1,
  "ownerId": 1, 
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "monthlyRent": 1500.00,
  "depositAmount": 3000.00
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

#### Payloads

**Payment Request:**
```json
{
  "rentalId": 1,
  "payerId": 1,
  "payeeId": 2,
  "amount": 1500.00,
  "currency": "USD" // Default: USD
}
```

### 5. Blockchain Integration Service
**Base Path:** `/api/blockchain`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/transaction` | Execute smart contract transaction | Yes |
| GET | `/contract/{address}` | Get smart contract state | Yes |

#### Payloads

**Smart Contract Request:**
```json
{
  "functionName": "payRent",
  "propertyId": 1,
  "walletAddress": "0xUserWallet...", // Optional
  "value": 1500.00, // Optional, for payable functions
  "contractAddress": "0xContract...", // Optional
  "parameters": { // Map of arguments
    "arg1": "value1",
    "arg2": 123
  }
}
```

### 6. Notification Service
**Base Path:** `/api/notifications`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/` | Send a notification (System use) | Yes |
| GET | `/user/{userId}` | Get user notifications | Yes |

#### Payloads

**Notification Request:**
```json
{
  "userId": 1,
  "recipientEmail": "user@example.com",
  "subject": "Payment Received",
  "message": "Your payment for January has been received."
}
```

## Enumerations

**Roles:**
`ADMIN`, `BUYER`, `SELLER`, `OWNER`, `TENANT`

**Property Types:**
`APARTMENT`, `HOUSE`, `VILLA`, `COMMERCIAL`

**Property Status:**
`AVAILABLE`, `RENTED`, `MAINTENANCE`

**Rental Status:**
`PENDING`, `ACTIVE`, `COMPLETED`, `CANCELLED`, `TERMINATED`
