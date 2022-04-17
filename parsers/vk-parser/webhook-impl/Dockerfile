FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN apk --no-cache add curl

ENV spring_profiles_active=docker \
    CONFIG_SERVER_URL=http://config:8888/ \
    SERVER_PORT=8000 \
    CALLBACK_URL=localhost/callback

EXPOSE ${SERVER_PORT}

CMD ["java", "-Xmx200m", "-jar", "app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=10s \
            CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1
