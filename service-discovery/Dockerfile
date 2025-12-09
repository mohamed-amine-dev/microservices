FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY common-library common-library
COPY service-discovery service-discovery
RUN mvn clean install -pl service-discovery -am -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/service-discovery/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
