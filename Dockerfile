# Сборка
# Используем официальный образ Maven с Java 17 (самая стабильная версия)
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar TelegramAlertBot.jar
ENTRYPOINT ["java", "-jar", "app.jar"]