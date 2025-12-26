FROM amazoncorretto:17-alpine
WORKDIR /app
COPY config-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
