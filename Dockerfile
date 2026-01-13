FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# ДОБАВЛЯЕМ SSL ПАРАМЕТРЫ для российских сайтов
ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.2,TLSv1.3 \
               -Djdk.tls.client.protocols=TLSv1.2,TLSv1.3 \
               -Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts \
               -Djavax.net.ssl.trustStorePassword=changeit"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]