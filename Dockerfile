FROM --platform=linux/amd64 eclipse-temurin:17

COPY build/libs/*SNAPSHOT.jar mpls-food.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-Dspring.profiles.active=production", "-jar", "mpls-food.jar" ]
