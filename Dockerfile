FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# В Alpine обновление сертификатов быстрое
RUN apk add --no-cache ca-certificates && update-ca-certificates

ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.2"

ENTRYPOINT ["java", "-jar", "app.jar"]