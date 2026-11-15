# Multi-stage Docker build for Java 21 Spring Boot Microservice
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
# Build and run contract tests
RUN apk add --no-cache maven && mvn clean test package -DskipTests=false

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:+ZGenerational", "-jar", "app.jar"]
