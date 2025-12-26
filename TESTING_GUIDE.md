# Real Estate Microservices - Testing Guide

## Prerequisites

Before running the application, ensure you have:

1. ✅ **PostgreSQL** running on `localhost:5432`
   - Username: `postgres`
   - Password: `postgres`
   - Databases created (see below)

2. **RabbitMQ** running on `localhost:5672`
   - Username: `guest`
   - Password: `guest`

### Create Required Databases

Connect to PostgreSQL and run:

```sql
CREATE DATABASE user_db;
CREATE DATABASE property_db;
CREATE DATABASE rental_db;
CREATE DATABASE payment_db;
CREATE DATABASE notification_db;
```

Or use the provided file:
```bash
psql -U postgres -f init-databases.sql
```

### Start RabbitMQ (if not installed)

```bash
docker run -d --name realestate-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine
```

## Starting the Application

### Option 1: Using the Startup Script (Recommended)

Simply run:
```bash
start-services.bat
```

This will start all services in the correct order with appropriate delays.

### Option 2: Manual Startup

Start services in this order:

1. **Service Discovery** (wait 30 seconds)
   ```bash
   cd service-discovery\target
   java -jar service-discovery-1.0.0.jar
   ```

2. **Config Server** (wait 20 seconds)
   ```bash
   cd config-server\target
   java -jar config-server-1.0.0.jar
   ```

3. **API Gateway** (wait 20 seconds)
   ```bash
   cd api-gateway\target
   java -jar api-gateway-1.0.0.jar
   ```

4. **Business Services** (can start in parallel)
   ```bash
   # User Service
   cd user-service\target
   java -jar user-service-1.0.0.jar
   
   # Property Service
   cd property-service\target
   java -jar property-service-1.0.0.jar
   
   # Rental Service
   cd rental-service\target
   java -jar rental-service-1.0.0.jar
   
   # Payment Service
   cd payment-service\target
   java -jar payment-service-1.0.0.jar
   
   # Notification Service
   cd notification-service\target
   java -jar notification-service-1.0.0.jar
   ```

## Verifying Services

### Check Service Discovery Dashboard
Open: http://localhost:8761

You should see all services registered.

### Check Individual Service Health
```bash
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8082/actuator/health  # Property Service
curl http://localhost:8083/actuator/health  # Rental Service
curl http://localhost:8084/actuator/health  # Payment Service
curl http://localhost:8086/actuator/health  # Notification Service
```

## Testing the APIs

### Option 1: Using the Test Script (Recommended)

Run the automated test script:
```bash
test-apis.bat
```

This will:
1. Register two users (owner and tenant)
2. Login both users and get JWT tokens
3. Create a property listing
4. Search for properties
5. Create a rental agreement
6. Process a payment

### Option 2: Manual Testing with curl

#### 1. Register Users

**Register Owner:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"owner@example.com\",\"password\":\"SecurePass123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"role\":\"OWNER\"}"
```

**Register Tenant:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"tenant@example.com\",\"password\":\"SecurePass456\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"role\":\"TENANT\"}"
```

#### 2. Login and Get JWT Tokens

**Login as Owner:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"owner@example.com\",\"password\":\"SecurePass123\"}"
```

Save the `token` from the response. You'll use it in subsequent requests.

#### 3. Create Property

```bash
curl -X POST http://localhost:8080/api/properties \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_OWNER_JWT_TOKEN" \
  -d "{\"title\":\"Luxury Downtown Apartment\",\"description\":\"Beautiful 2-bedroom apartment with city views\",\"price\":2000.00,\"address\":\"123 Main Street\",\"city\":\"New York\",\"country\":\"USA\",\"type\":\"APARTMENT\",\"bedrooms\":2,\"bathrooms\":2,\"area\":95.5,\"images\":[]}"
```

#### 4. Search Properties (No auth needed)

```bash
curl "http://localhost:8080/api/properties/search?city=New York&minPrice=1000&maxPrice=3000"
```

#### 5. Create Rental Agreement

First, login as tenant to get tenant JWT token:
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"tenant@example.com\",\"password\":\"SecurePass456\"}"
```

Then create rental:
```bash
curl -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TENANT_JWT_TOKEN" \
  -d "{\"propertyId\":1,\"tenantId\":2,\"startDate\":\"2025-01-01\",\"endDate\":\"2025-12-31\",\"monthlyRent\":2000.00}"
```

#### 6. Process Payment

```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TENANT_JWT_TOKEN" \
  -d "{\"rentalId\":1,\"payerId\":2,\"amount\":2000.00,\"currency\":\"USD\",\"method\":\"CREDIT_CARD\"}"
```

## Service Ports

| Service | Port |
|---------|------|
| Service Discovery (Eureka) | 8761 |
| Config Server | 8888 |
| API Gateway | 8080 |
| User Service | 8081 |
| Property Service | 8082 |
| Rental Service | 8083 |
| Payment Service | 8084 |
| Blockchain Integration | 8085 |
| Notification Service | 8086 |
| PostgreSQL | 5432 |
| RabbitMQ | 5672 |
| RabbitMQ Management UI | 15672 |

## Troubleshooting

### Services won't start
- Check if PostgreSQL is running and accessible
- Check if RabbitMQ is running
- Ensure databases are created
- Check logs in each service window for errors

### Database connection errors
- Verify PostgreSQL is running on port 5432
- Verify credentials (postgres/postgres)
- Verify databases exist

### RabbitMQ connection errors
- Verify RabbitMQ is running on port 5672
- Check RabbitMQ management UI: http://localhost:15672 (guest/guest)

### Services not registering with Eureka
- Ensure Service Discovery started first
- Wait at least 30-60 seconds after starting a service
- Check service logs for connection errors

## Expected Response Format

All API responses follow this format:

**Success:**
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { /* actual response data */ }
}
```

**Error:**
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

## Next Steps

After successful testing, you can:
1. Review the API documentation in `FRONTEND_INTEGRATION_GUIDE.md`
2. Explore the Swagger UI for each service at `/swagger-ui.html`
3. Monitor services via Eureka Dashboard
4. Check RabbitMQ messages in the Management UI
5. Deploy to Docker using `docker-compose up`
