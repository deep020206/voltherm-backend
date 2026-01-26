# -------------------
# Build stage (Maven + JDK)
# -------------------
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B -q
COPY src ./src
RUN mvn clean package -DskipTests -B -q

# -------------------
# Runtime stage (Distroless minimal JRE)
# -------------------
FROM gcr.io/distroless/java21
WORKDIR /app
COPY --from=build /app/target/voltherm-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
