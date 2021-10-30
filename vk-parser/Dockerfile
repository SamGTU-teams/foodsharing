FROM openjdk:8-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java", "-Xmx200m", "-jar", "/app.jar"]

EXPOSE 8000
