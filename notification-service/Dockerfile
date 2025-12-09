FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY common-library common-library
COPY notification-service notification-service
RUN mvn clean install -pl notification-service -am -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
