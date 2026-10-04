# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (layer cache optimization)
RUN --mount=type=cache,target=/root/.m2/repository \
    ./mvnw dependency:go-offline -B

# Build the application
RUN ./mvnw package -DskipTests -B

# Runtime stage — Oracle Linux 9 (ULN/ARM compatible)
FROM oraclelinux:9

LABEL maintainer="kalenz.io"
LABEL description="BMS Service"

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV APP_HOME=/app

# Install OpenJDK 21
RUN dnf install -y \
        java-21-openjdk \
        java-21-openjdk-devel \
    && dnf clean all \
    && java -version

WORKDIR ${APP_HOME}

# Create non-root user for security
RUN useradd --create-home --shell /bin/bash appuser
USER appuser

# Copy the fat JAR from build stage
COPY --from=builder /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
