# Stage 1: Build
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn -pl blockchain-integration-service -am clean package -DskipTests

# Stage 2: Run
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/blockchain-integration-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
