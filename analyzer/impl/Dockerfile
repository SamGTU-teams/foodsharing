FROM python:3-alpine

WORKDIR /app

ENV spring_profiles_active=docker \
    RABBITMQ_USER=guest \
    RABBITMQ_PASS=guest \
    DATABASE_NAME=postgres \
    DATABASE_USER=postgres \
    DATABASE_PASS=postgres \
    SERVER_PORT=8010

COPY requirements.txt ./
COPY ./analyzer ./analyzer
COPY ./resources ./resources

RUN apk --no-cache add curl && \
    pip install --no-cache-dir -r requirements.txt

CMD [ "python", "analyzer/manage.py" ]

#HEALTHCHECK --interval=30s \
#            --timeout=5s \
#            --retries=5 \
#            --start-period=30s \
#            CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

EXPOSE ${SERVER_PORT}
