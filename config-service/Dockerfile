FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV spring_profiles_active=native,docker \
    CONFIG_DIR=/app/shared \
    SERVER_PORT=8888

RUN apk --no-cache add curl && \
    mkdir -p ${CONFIG_DIR}

EXPOSE ${SERVER_PORT}
VOLUME ${CONFIG_DIR}

CMD ["java", "-Xmx200m", "-jar", "app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=10s \
            CMD curl --fail --silent localhost:${SERVER_PORT}/actuator/health | grep UP || exit 1
