FROM amazoncorretto:17-alpine
WORKDIR /app
COPY payment-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
