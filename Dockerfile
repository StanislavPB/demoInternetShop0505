FROM maven:3.8.8-eclipse-temurin-17 AS build
COPY src/main/java /home/src
WORKDIR /home/src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre AS runtime
EXPOSE 8080

RUN mkdir /app
COPY --from=build /home/src/target/*.jar /app/project.jar

ENTRYPOINT ["java", "-jar", "/app/project.jar"]

