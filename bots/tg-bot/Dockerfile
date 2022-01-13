FROM openjdk:8-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV spring_profiles_active=docker

CMD ["java", "-Xmx200m", "-jar", "/app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=30s \
            CMD curl -f http://localhost:8030/actuator/health || exit 1

EXPOSE 8030
