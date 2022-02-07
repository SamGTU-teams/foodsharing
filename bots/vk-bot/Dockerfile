FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

ENV SPR_PROFILE=docker \
    CONFIG_SERVER_URL=http://config:8888/ \
    VK_BOT_GROUP_ID=12345 \
    VK_BOT_ACCESS_TOKEN=TEST \
    SERVER_PORT=8040

ARG JAR_FILE=target/*.jar

RUN apk --no-cache add curl

COPY ${JAR_FILE} app.jar

CMD ["java", "-Xmx200m", "-jar", "app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=30s \
            CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

EXPOSE ${SERVER_PORT}
