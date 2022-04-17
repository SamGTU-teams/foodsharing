FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

RUN apk --no-cache add curl

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV spring_profiles_active=docker \
    CONFIG_SERVER_URL=http://config:8888/ \
    VK_BOT_GROUP_ID=12345 \
    VK_BOT_ACCESS_TOKEN=TEST \
    SERVER_PORT=8040

EXPOSE ${SERVER_PORT}

CMD ["java", "-Xmx200m", "-jar", "app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=10s \
            CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1
