FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY common-library common-library
COPY config-server config-server
RUN mvn clean install -pl config-server -am -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/config-server/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
