# Use Eclipse Temurin 17 JDK
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR into the container
COPY target/apiGetway-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the API Gateway runs on
EXPOSE 8082

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker run -p 8082:8082 -e SCHOOL_SERVICE_URL="http://host.docker.internal:8080" api_gateway_app
