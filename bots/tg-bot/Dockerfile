FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN apk --no-cache add curl

ENV spring_profiles_active=docker \
    CONFIG_SERVER_URL=http://config:8888/ \
    TG_BOT_WEB_HOOK_PATH=TEST \
    TG_BOT_USERNAME=TEST \
    TG_BOT_TOKEN=TEST \
    SERVER_PORT=8030

EXPOSE ${SERVER_PORT}

CMD ["java", "-Xmx200m", "-jar", "app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=30s \
            CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1
