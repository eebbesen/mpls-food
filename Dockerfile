FROM --platform=linux/amd64 eclipse-temurin:17

RUN mkdir /app
WORKDIR /app
COPY build/libs/*SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-Dspring.profiles.active=production", "-jar", "app.jar" ]
