FROM amazoncorretto:17-alpine
WORKDIR /app
COPY blockchain-integration-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
