FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

RUN apk --no-cache add curl

ENV spring_profiles_active=docker \
    CONFIG_SERVER_URL=http://config/ \
    SERVER_PORT=80

EXPOSE ${SERVER_PORT}

HEALTHCHECK --interval=30s \
            --timeout=5s \
            --retries=5 \
            --start-period=10s \
            CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV LUCENE_DIR=/lucene/indexDir

VOLUME ${LUCENE_DIR}

CMD ["java", "-Xmx200m", "-jar", "app.jar"]
