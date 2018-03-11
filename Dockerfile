FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 2002
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]