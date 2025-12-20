# Stage 1: Build
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn -pl service-discovery -am clean package -DskipTests

# Stage 2: Run
FROM amazoncorretto:17-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=build /app/service-discovery/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
