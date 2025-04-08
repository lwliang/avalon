FROM ubuntu:latest
FROM openjdk:15-jdk-slim
LABEL authors="avalon"

WORKDIR /avalon

COPY target/avalon-file.jar /avalon/avalon-file.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar","/avalon/avalon-file.jar"]