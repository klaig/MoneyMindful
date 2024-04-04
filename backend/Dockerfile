# Stage 1: Build the application
FROM amazoncorretto:21 as build

# Add Maintainer Info
LABEL maintainer="kevin@example.com"

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Gradle configuration files and wrapper
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Convert line endings and grant execution permissions to the Gradle Wrapper
RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

# Copy Checkstyle configuration
COPY google_checks.xml /app/

# Copy the source code
COPY src /app/src

# Build the application
RUN ./gradlew build -x test

# Stage 2: Create the runtime image
FROM amazoncorretto:21

# Copy the built jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
