FROM amazoncorretto:17-alpine
WORKDIR /app
COPY property-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
