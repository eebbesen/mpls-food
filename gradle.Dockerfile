FROM gradle:8.5-jdk17
WORKDIR /home/gradle
USER gradle
COPY build.gradle .
COPY src src

RUN gradle build --no-scan -PexcludeTests=**/endtoend*

EXPOSE 8080

ENTRYPOINT java -jar build/libs/*SNAPSHOT.jar
