# Real Estate Rental Platform - Frontend Integration Guide

## 📋 For Frontend Developers

This document provides everything you need to integrate with the Real Estate Rental Platform backend.

---

## 🚀 Quick Start

### Base URLs

**Production/Development:**
- API Gateway (all requests): `http://localhost:8080`
- Eureka Dashboard: `http://localhost:8761`

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
  "role": "OWNER",  // "OWNER", "TENANT", or "ADMIN"
  "walletAddress": "0x1234...abcd"  // optional
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

**Response:**
Returns a JWT token. You must include this token in the `Authorization` header as `Bearer <token>` for all protected endpoints.

---

## 🏠 Property Management API

### List All Properties / Search
`GET /api/properties/search`
Query params: `city`, `minPrice`, `maxPrice`, `bedrooms`, `status`.

### Get Single Property
`GET /api/properties/{id}`

### Create Property (Owner Only)
`POST /api/properties`

### Update Property
`PUT /api/properties/{id}`

---

## 🏢 Rental Management API

### Create Rental Request (Tenant)
`POST /api/rentals`
**Request:**
```json
{
  "propertyId": 1,
  "tenantId": 101, // In real app, derived from Token
  "ownerId": 202,
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "monthlyRent": 1500.0,
  "depositAmount": 3000.0
}
```
*Note: This endpoint triggers an async notification to the Tenant.*

### Get Rentals
- By Tenant: `GET /api/rentals/tenant/{tenantId}`
- By Owner: `GET /api/rentals/owner/{ownerId}`

---

## 🔔 Notification API

### Get User Notifications
`GET /api/notifications/user/{userId}`

**Response:**
```json
{
  "status": 200,
  "data": [
    {
      "id": 1,
      "recipientEmail": "tenant@example.com",
      "subject": "Rental Agreement Created",
      "message": "Your rental agreement for property 1 has been created.",
      "status": "SENT",
      "createdAt": "2025-01-01T10:00:00"
    }
  ]
}
```

### Send Notification (Testing)
`POST /api/notifications`
This is primarily internal, but exposed for testing.

---

## ⛓️ Blockchain API

### Execute Smart Contract Transaction
`POST /api/blockchain/transaction`
**Request:**
```json
{
  "functionName": "createRentalAgreement",
  "propertyId": 1,
  "value": 1500.00
}
```

### Get Contract State
`GET /api/blockchain/contract/{address}`

---

## 💳 Payment API

### Process Payment
`POST /api/payments`
**Request:**
```json
{
  "rentalId": 1,
  "payerId": 101,
  "payeeId": 202,
  "amount": 1500.00,
  "currency": "ETH"
}
```

### Get Payment History
`GET /api/payments/rental/{rentalId}`

---

## ⚠️ Important Notes

1. **Async Notifications**: The backend uses RabbitMQ. When a Rental is created, a notification is asynchronously generated. The frontend can poll `/api/notifications/user/{id}` to see them.
2. **Blockchain Mock**: The blockchain service currently returns mocked transaction hashes (`0x...`). This allows you to build the UI flow without waiting for a real Ethereum transaction.

