FROM amazoncorretto:17-alpine
WORKDIR /app
COPY user-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
