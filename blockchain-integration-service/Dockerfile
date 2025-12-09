FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY common-library common-library
COPY blockchain-integration-service blockchain-integration-service
RUN mvn clean install -pl blockchain-integration-service -am -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/blockchain-integration-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
