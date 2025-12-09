# Real Estate Rental Platform - Microservices Backend

A production-ready Spring Boot 3.x multi-module microservices application for a decentralized real estate rental platform with blockchain integration.

## 🏗️ Architecture

This application consists of 10 microservices:

### Infrastructure Services
- **Service Discovery** (Port 8761) - Eureka Server for service registration
- **Config Server** (Port 8888) - Centralized configuration management
- **API Gateway** (Port 8080) - Single entry point with JWT authentication

### Business Services
- **User Service** (Port 8081) - User authentication and management
- **Property Service** (Port 8082) - Property listing and management
- **Rental Service** (Port 8083) - Rental agreements and lease management
- **Payment Service** (Port 8084) - Payment tracking
- **Blockchain Integration Service** (Port 8085) - Web3 and smart contract interactions
- **Notification Service** (Port 8086) - Email and push notifications

### Common Library
- Shared DTOs, exceptions, and utilities

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java**: 17 (OpenJDK)
- **Build Tool**: Maven (Multi-module)
- **Database**: PostgreSQL 15+
- **Message Broker**: RabbitMQ
- **API Gateway**: Spring Cloud Gateway
- **Service Discovery**: Netflix Eureka
- **Security**: Spring Security with JWT
- **Blockchain**: Web3j
- **Documentation**: OpenAPI/Swagger 3.0
- **Containerization**: Docker & Docker Compose

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- PostgreSQL 15+ (if running locally without Docker)
- RabbitMQ (if running locally without Docker)

## 🚀 Quick Start with Docker

### 1. Clone the repository
```bash
git clone <repository-url>
cd Anti-project
```

### 2. Build all modules
```bash
mvn clean install -DskipTests
```

### 3. Start all services with Docker Compose
```bash
docker-compose up -d
```

### 4. Verify services are running
- Eureka Dashboard: http://localhost:8761
- RabbitMQ Management: http://localhost:15672 (guest/guest)
- API Gateway: http://localhost:8080

## 🔧 Running Locally (Without Docker)

### 1. Start PostgreSQL and create databases
```sql
CREATE DATABASE user_db;
CREATE DATABASE property_db;
CREATE DATABASE rental_db;
CREATE DATABASE payment_db;
CREATE DATABASE notification_db;
```

### 2. Start RabbitMQ
```bash
rabbitmq-server
```

### 3. Start services in order
```bash
# Terminal 1 - Service Discovery
cd service-discovery
mvn spring-boot:run

# Terminal 2 - Config Server
cd config-server
mvn spring-boot:run

# Terminal 3 - API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 4 - User Service
cd user-service
mvn spring-boot:run

# Terminal 5 - Property Service
cd property-service
mvn spring-boot:run

# Continue for other services...
```

## 📖 API Documentation

Each service exposes Swagger UI for API documentation:

- User Service: http://localhost:8081/swagger-ui.html
- Property Service: http://localhost:8082/swagger-ui.html
- Rental Service: http://localhost:8083/swagger-ui.html
- Payment Service: http://localhost:8084/swagger-ui.html
- Blockchain Service: http://localhost:8085/swagger-ui.html
- Notification Service: http://localhost:8086/swagger-ui.html

## 🔐 Authentication

### Register a new user
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "OWNER",
    "walletAddress": "0x1234567890abcdef1234567890abcdef12345678"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Use the returned JWT token in the `Authorization: Bearer <token>` header for authenticated requests.

## 🏠 Property Management

### Create a property
```bash
curl -X POST http://localhost:8080/api/properties \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "title": "Modern Apartment",
    "description": "2BR apartment in city center",
    "address": "123 Main St",
    "city": "New York",
    "country": "USA",
    "pricePerMonth": 2000,
    "depositAmount": 4000,
    "bedroomCount": 2,
    "bathroomCount": 1,
    "squareMeters": 80,
    "propertyType": "APARTMENT",
    "ownerId": 1
  }'
```

### Search properties
```bash
curl "http://localhost:8080/api/properties/search?city=New York&minPrice=1000&maxPrice=3000&bedrooms=2"
```

## ⛓️ Blockchain Configuration

Update the blockchain service configuration in `blockchain-integration-service/src/main/resources/application.yml`:

```yaml
blockchain:
  ethereum:
    network-url: https://sepolia.infura.io/v3/YOUR_INFURA_PROJECT_ID
    private-key: YOUR_PRIVATE_KEY_HERE
```

## 📧 Email Configuration

Update the notification service configuration in `notification-service/src/main/resources/application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    username: YOUR_EMAIL@gmail.com
    password: YOUR_APP_PASSWORD
```

## 🗄️ Database Migrations

Database migrations are handled automatically by Flyway on application startup. Migration scripts are located in each service's `src/main/resources/db/migration` directory.

## 🧪 Testing

Run unit tests for all modules:
```bash
mvn test
```

Run integration tests:
```bash
mvn verify
```

## 📊 Monitoring

### Health Checks
All services expose health endpoints via Spring Boot Actuator:
```bash
curl http://localhost:8081/actuator/health
```

### Service Registry
View all registered services:
- http://localhost:8761

## 🔄 Inter-Service Communication

- **Synchronous**: OpenFeign clients with Resilience4j circuit breakers
- **Asynchronous**: RabbitMQ message broker

## 🐳 Docker Commands

### Build and Start
```bash
docker-compose up -d
```

### View Logs
```bash
docker-compose logs -f <service-name>
```

### Stop All Services
```bash
docker-compose down
```

### Rebuild a Service
```bash
docker-compose up -d --build <service-name>
```

## 🛡️ Security

- JWT-based authentication
- BCrypt password encryption
- API Gateway validates all incoming requests
- Public endpoints: `/api/users/register`, `/api/users/login`

## 📁 Project Structure

```
real-estate-rental-backend/
├── common-library/
├── service-discovery/
├── config-server/
├── api-gateway/
├── user-service/
├── property-service/
├── rental-service/
├── payment-service/
├── blockchain-integration-service/
├── notification-service/
├── docker-compose.yml
├── init-databases.sql
└── pom.xml
```

## 🐛 Troubleshooting

### Services won't start
- Ensure PostgreSQL and RabbitMQ are running
- Check port conflicts
- Verify Eureka Server is up before starting other services

### Database connection errors
- Verify database credentials in application.yml
- Ensure databases are created
- Check PostgreSQL is accepting connections

### JWT token errors
- Ensure the same secret key is used in API Gateway and User Service
- Check token expiration time

## 📝 License

This project is licensed under the MIT License.

## 👥 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Contact

For questions or support, please open an issue on GitHub.
