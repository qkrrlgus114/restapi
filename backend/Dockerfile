FROM openjdk:17-alpine

ADD https://repo1.maven.org/maven2/com/navercorp/pinpoint/pinpoint-agent/3.0.0/pinpoint-agent-3.0.0.tar.gz /usr/local/
RUN tar -zxvf /usr/local/pinpoint-agent-3.0.0.tar.gz -C /usr/local

ARG JAR_FILE=/build/libs/restapi-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /restapi.jar

ENTRYPOINT ["java","-jar","/restapi.jar"]