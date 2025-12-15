
FROM eclipse-temurin:17-jdk

WORKDIR /app


COPY target/school-monolith-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


# docker pull eclipse-temurin:17-jdk
# docker run -p 8080:8080 -e SPRING_DATASOURCE_URL="jdbc:sqlserver://host.docker.internal:1433;databaseName=School;encrypt=true;trustServerCertificate=true" -e SPRING_DATASOURCE_USERNAME=SA -e SPRING_DATASOURCE_PASSWORD=Mariam@123 school_service_app
