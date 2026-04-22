## Multi-stage build for Spring Boot (Maven Wrapper)
## 1) Build stage
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

# Copy Maven wrapper and pom first for better layer caching
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY .mvn .mvn
COPY pom.xml pom.xml


# Copy source and build
COPY src src
RUN ./mvnw -q -DskipTests package


## 2) Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the fat jar produced by spring-boot-maven-plugin
COPY --from=builder /workspace/target/*.jar /app/app.jar

# App listens on 8081 (as per application.properties)
EXPOSE 8081

# Optionally allow overriding Spring properties via env
# Example:
#   -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/book
#   -e SPRING_DATASOURCE_USERNAME=postgres
#   -e SPRING_DATASOURCE_PASSWORD=root
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

