FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY common-library common-library
COPY user-service user-service
RUN mvn clean install -pl user-service -am -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
