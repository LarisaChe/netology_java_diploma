FROM openjdk:17-jdk-alpine

EXPOSE 8090

ADD build/libs/netology_java_diploma-0.0.1-SNAPSHOT-plain.jar cloud.jar

ENTRYPOINT ["java","-jar","/cloud.jar"]