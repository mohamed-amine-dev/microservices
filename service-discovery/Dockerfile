FROM amazoncorretto:17-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY service-discovery/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
