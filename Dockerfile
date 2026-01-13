FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java",
 "-Djdk.tls.client.protocols=TLSv1.2",
 "-Dhttps.protocols=TLSv1.2",
 "-jar", "app.jar"]