FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/springboot_docker-1.0-SNAPSHOT.jar docker.jar
ENTRYPOINT ["java","-jar","/docker.jar"]