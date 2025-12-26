@echo off
REM Startup script for Real Estate Microservices

echo ========================================
echo Starting Real Estate Microservices
echo ========================================

echo.
echo [1/6] Starting Service Discovery (Port 8761)...
start "Service Discovery" cmd /k "cd service-discovery\target && java -jar service-discovery-1.0.0.jar"
timeout /t 30 /nobreak

echo.
echo [2/6] Starting Config Server (Port 8888)...
start "Config Server" cmd /k "cd config-server\target && java -jar config-server-1.0.0.jar"
timeout /t 20 /nobreak

echo.
echo [3/6] Starting API Gateway (Port 8080)...
start "API Gateway" cmd /k "cd api-gateway\target && java -jar api-gateway-1.0.0.jar"
timeout /t 20 /nobreak

echo.
echo [4/6] Starting User Service (Port 8081)...
start "User Service" cmd /k "cd user-service\target && java -jar user-service-1.0.0.jar"

echo.
echo [5/6] Starting Property Service (Port 8082)...
start "Property Service" cmd /k "cd property-service\target && java -jar property-service-1.0.0.jar"

echo.
echo [6/6] Starting Rental Service (Port 8083)...
start "Rental Service" cmd /k "cd rental-service\target && java -jar rental-service-1.0.0.jar"

echo.
echo [7/8] Starting Payment Service (Port 8084)...
start "Payment Service" cmd /k "cd payment-service\target && java -jar payment-service-1.0.0.jar"

echo.
echo [8/8] Starting Notification Service (Port 8086)...
start "Notification Service" cmd /k "cd notification-service\target && java -jar notification-service-1.0.0.jar"

echo.
echo ========================================
echo All services are starting!
echo ========================================
echo.
echo Check Service Discovery at: http://localhost:8761
echo Check API Gateway at: http://localhost:8080
echo.
echo Wait 1-2 minutes for all services to fully start.
echo Then run test-apis.bat to test the application.
echo.
pause
