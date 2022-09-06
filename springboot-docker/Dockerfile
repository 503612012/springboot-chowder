FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/springboot-docker-1.0.0.jar docker.jar
ENTRYPOINT ["java","-jar","/docker.jar"]