# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy gradle wrapper and necessary configuration files
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Make the wrapper executable
RUN chmod +x ./gradlew

# Resolve dependencies (caching step)
RUN ./gradlew dependencies --no-daemon

# Copy source code and build the application
COPY src src
RUN ./gradlew bootJar --no-daemon && \
    find build/libs -name "*.jar" ! -name "*plain.jar" -exec cp {} /app/application.jar \;

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Add a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Install required LaTeX dependencies and clean up
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    texlive-latex-base \
    texlive-latex-extra \
    texlive-fonts-recommended && \
    rm -rf /var/lib/apt/lists/*

# Copy the built jar from the builder stage
COPY --from=builder /app/application.jar app.jar
RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
