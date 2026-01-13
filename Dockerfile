# Сборка
# Используем официальный образ Maven с Java 17 (самая стабильная версия)
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre

RUN apt-get update && \
    apt-get install -y ca-certificates && \
    update-ca-certificates

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.2,TLSv1.3 -Djdk.tls.client.protocols=TLSv1.2,TLSv1.3"
ENTRYPOINT ["java", "-jar", "app.jar"]