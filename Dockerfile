
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/task-management.jar task-management.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "task-management.jar"]