FROM eclipse-temurin:21-jre

RUN apt-get update && \
    apt-get install -y --no-install-recommends texlive-latex-base texlive-latex-extra texlive-fonts-recommended && \
    rm -rf /var/lib/apt/lists/*

COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
