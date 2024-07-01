FROM openjdk:17-alpine

ARG JAR_FILE=/build/libs/restapi-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /restapi.jar

ENTRYPOINT ["java","-jar","/restapi.jar"]