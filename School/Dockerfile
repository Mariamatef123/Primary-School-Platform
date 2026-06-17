FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
COPY apiGetway/pom.xml apiGetway/pom.xml
COPY School/pom.xml School/pom.xml
COPY School/src School/src

RUN mvn -B -pl School -am -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app

RUN mkdir -p /app/logs

COPY --from=build /workspace/School/target/school-monolith-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.devtools.restart.enabled=true", "-jar", "app.jar"]