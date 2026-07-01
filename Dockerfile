# Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy Maven wrapper and pom first to cache dependencies
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests dependency:go-offline

# Copy source and build application
COPY src ./src
RUN ./mvnw -B -DskipTests package

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends locales tzdata && \
    locale-gen cs_CZ.UTF-8 && \
    update-locale LANG=cs_CZ.UTF-8 LANGUAGE=cs_CZ:cs LC_ALL=cs_CZ.UTF-8 && \
    rm -rf /var/lib/apt/lists/*

ENV LANG=cs_CZ.UTF-8 \
    LANGUAGE=cs_CZ:cs \
    LC_ALL=cs_CZ.UTF-8 \
    JAVA_TOOL_OPTIONS="-Duser.language=cs -Duser.country=CZ"

COPY --from=build /workspace/target/sfgame-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
