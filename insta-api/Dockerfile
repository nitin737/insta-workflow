# Build stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /build

# Copy gradle wrapper and config
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

# Make gradlew executable
RUN chmod +x gradlew
# Download dependencies
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies

# Copy source code
COPY src src/
# Build the application
RUN --mount=type=cache,target=/root/.gradle \
    --mount=type=cache,target=/build/.gradle \
    ./gradlew bootJar -x test

# Runtime stage
# We use the official Playwright Java image to ensure all browser OS dependencies are present.
# Distroless or Alpine would crash when trying to launch Chromium.
FROM mcr.microsoft.com/playwright/java:v1.44.0-jammy
WORKDIR /app

# Change ownership to pwuser so it can write to /app (e.g. for output directory)
RUN chown -R pwuser:pwuser /app

# Run as a non-root user for security (Playwright image has a 'pwuser')
USER pwuser

# Copy the built jar
COPY --from=build /build/build/libs/*SNAPSHOT.jar app.jar

ENV SPRING_BOOT_PORT=8081
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
