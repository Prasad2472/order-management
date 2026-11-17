
# Stage 1: Build
FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean verify

RUN set -eux; \
    JAR=$(find target -maxdepth 1 -type f \
        -name '*.jar' ! -name '*.jar.original' | head -n 1); \
    test -n "$JAR"; \
    cp "$JAR" /app/app.jar


# Stage 2: Runtime
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN groupadd --system app && \
    useradd --system --gid app --uid 10001 \
    --create-home --home-dir /home/app app

# Create writable directories
RUN mkdir -p /app/data && \
    chown -R app:app /app /home/app

COPY --from=build --chown=app:app /app/app.jar app.jar

EXPOSE 8080

USER app

ENTRYPOINT ["java", "-jar", "app.jar"]