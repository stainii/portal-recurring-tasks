FROM openjdk:11.0.1-jdk-sid
VOLUME /tmp
EXPOSE 2002
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS_HOUSAGOTCHI -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.datasource.password=${POSTGRES_PASSWORD} --eureka.client.service-url.defaultZone=${EUREKA_SERVICE_URL}
