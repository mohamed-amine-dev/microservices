# Testing Your Microservices - Quick Start Guide

## Current Status
✅ All 10 microservices created
✅ Docker and Docker Compose installed
❌ Maven not installed (required for building)

## 🚀 Option 1: Install Maven and Build (Recommended)

### Step 1: Install Maven
Download and install Maven from: https://maven.apache.org/download.cgi

Or use chocolatey (if installed):
```powershell
choco install maven
```

### Step 2: Build the Project
```bash
mvn clean install -DskipTests
```

### Step 3: Start with Docker Compose
```bash
docker-compose up -d
```

### Step 4: Verify Services
- Eureka Dashboard: http://localhost:8761
- RabbitMQ Management: http://localhost:15672 (guest/guest)
- User Service Swagger: http://localhost:8081/swagger-ui.html
- Property Service Swagger: http://localhost:8082/swagger-ui.html

---

## 🎯 Option 2: Run Individual Services Manually (Without Docker)

If you have Java 17 installed but not Maven, you can use Maven Wrapper:

### Step 1: Initialize Maven Wrapper
```bash
mvn -N wrapper:wrapper
```

### Step 2: Build with Wrapper
```bash
./mvnw clean install -DskipTests
```

### Step 3: Start PostgreSQL
Install PostgreSQL 15 and create databases:
```sql
CREATE DATABASE user_db;
CREATE DATABASE property_db;
CREATE DATABASE rental_db;
CREATE DATABASE payment_db;
CREATE DATABASE notification_db;
```

### Step 4: Start RabbitMQ
Download from: https://www.rabbitmq.com/download.html

### Step 5: Run Services in Order
```bash
# Terminal 1 - Service Discovery
cd service-discovery
java -jar target/service-discovery-1.0.0.jar

# Terminal 2 - Config Server (wait 30 seconds after Eureka)
cd config-server
java -jar target/config-server-1.0.0.jar

# Terminal 3 - API Gateway
cd api-gateway
java -jar target/api-gateway-1.0.0.jar

# Terminal 4 - User Service
cd user-service
java -jar target/user-service-1.0.0.jar

# Continue for other services...
```

---

## 🧪 Option 3: Quick Test Without Building (IDE)

If you have IntelliJ IDEA or Eclipse:

1. **Open Project** in your IDE
2. **Let Maven download dependencies** (automatic)
3. **Run each Application class**:
   - `ServiceDiscoveryApplication.java`
   - `ConfigServerApplication.java`
   - `ApiGatewayApplication.java`
   - `UserServiceApplication.java`
   - etc.

---

## ✅ How to Verify Services Are Working

### 1. Check Service Registration
Visit: http://localhost:8761

You should see all services registered:
- API-GATEWAY
- USER-SERVICE
- PROPERTY-SERVICE
- RENTAL-SERVICE
- PAYMENT-SERVICE
- BLOCKCHAIN-INTEGRATION-SERVICE
- NOTIFICATION-SERVICE

### 2. Test User Service

**Register a User:**
```bash
curl -X POST http://localhost:8080/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"role\":\"OWNER\"}"
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/users/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}"
```

You should receive a JWT token in response.

### 3. Test Property Service

Use the JWT token from login:
```bash
curl -X POST http://localhost:8080/api/properties ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_JWT_TOKEN" ^
  -d "{\"title\":\"Modern Apartment\",\"address\":\"123 Main St\",\"city\":\"New York\",\"country\":\"USA\",\"pricePerMonth\":2000,\"depositAmount\":4000,\"bedroomCount\":2,\"bathroomCount\":1,\"squareMeters\":80,\"propertyType\":\"APARTMENT\",\"ownerId\":1}"
```

### 4. Check Health Endpoints

All services expose health endpoints:
```bash
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8082/actuator/health  # Property Service
curl http://localhost:8083/actuator/health  # Rental Service
```

### 5. Check Logs

**With Docker:**
```bash
docker-compose logs -f user-service
docker-compose logs -f property-service
```

**Without Docker:**
Check console output in each terminal

---

## 🐛 Common Issues

### Database Connection Errors
- Ensure PostgreSQL is running on port 5432
- Verify databases are created
- Check username/password in application.yml

### Port Already in Use
- Check if ports 8080-8086, 5432, 5672, 8761, 8888 are available
- Stop conflicting services

### Services Not Registering with Eureka
- Ensure Eureka Server (port 8761) starts first
- Wait 30-60 seconds for registration

### JWT Token Errors
- Ensure same JWT secret in api-gateway and user-service

---

## 📊 Expected Results

When everything is working:

✅ **Eureka Dashboard** shows all 7 services registered
✅ **RabbitMQ Management** shows exchanges and queues
✅ **User registration** returns 201 Created with user data
✅ **Login** returns JWT token
✅ **Property creation** with JWT works
✅ **Health endpoints** return status "UP"
✅ **Swagger UI** is accessible for each service

---

## 🎯 Recommended Next Steps

1. **Install Maven** (easiest path)
2. **Build the project**: `mvn clean install -DskipTests`
3. **Start infrastructure**: `docker-compose up -d postgres rabbitmq`
4. **Start services**: `docker-compose up -d` (or run individually)
5. **Test endpoints** using the curl commands above
6. **Check Swagger UI** for interactive API testing

---

## 💡 Pro Tip

Use **Postman** or **Insomnia** for easier API testing than curl commands!
