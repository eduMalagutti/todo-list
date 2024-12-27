# Stage 1: Build the application using Maven
FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven

# Set working directory
WORKDIR /app

# Copy Maven configuration and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean install -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /todolist

# Expose application port
EXPOSE 8080

# Copy the built JAR from the build stage
COPY --from=build /app/target/todolist-1.0.0.jar app.jar

# Define entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
