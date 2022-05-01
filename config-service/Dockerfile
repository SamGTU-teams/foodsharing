FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

RUN apk --no-cache add curl

ENV spring_profiles_active=native,docker \
    CONFIG_DIR=/app/shared \
    SERVER_PORT=80

EXPOSE ${SERVER_PORT}

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=10s \
            CMD curl --fail --silent localhost:${SERVER_PORT}/actuator/health | grep UP || exit 1

RUN mkdir -p ${CONFIG_DIR}
VOLUME ${CONFIG_DIR}

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java", "-Xmx200m", "-jar", "app.jar"]
