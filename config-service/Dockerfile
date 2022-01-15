FROM openjdk:8-alpine

WORKDIR /app

ENV spring_profiles_active=native,docker \
    CONFIG_DIR=/app/shared \
    SERVER_PORT=8888

ARG JAR_FILE=target/*.jar

RUN apk --no-cache add curl && \
    mkdir -p ${CONFIG_DIR}

COPY ${JAR_FILE} app.jar

VOLUME ${CONFIG_DIR}

CMD ["java", "-Xmx200m", "-jar", "app.jar"]

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --start-period=30s \
            --retries=5 \
            CMD curl --fail --silent localhost:${SERVER_PORT}/actuator/health | grep UP || exit 1

EXPOSE ${SERVER_PORT}
