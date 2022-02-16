FROM python:3-alpine

WORKDIR /app

ENV SPR_PROFILE=docker \
    CONFIG_SERVER_URL=http://config:8888/ \
    SERVER_PORT=8010

COPY requirements.txt ./
COPY ./old_analyzer ./analyzer
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
