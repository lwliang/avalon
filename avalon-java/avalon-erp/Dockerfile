FROM ubuntu:latest
FROM openjdk:15-jdk-slim
LABEL authors="avalon"

WORKDIR /avalon

COPY target/avalon-server.jar /avalon/avalon-server.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar","/avalon/avalon-server.jar"]