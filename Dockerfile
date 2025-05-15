# Use OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy and build the project
COPY . .

RUN chmod +x ./gradlew
# Build the application (you can skip this if you push the built JAR)
RUN ./gradlew build --no-daemon

# Run the application
CMD ["java", "-jar", "build/libs/vastrarent-0.0.1-SNAPSHOT.jar"]
