FROM python:3-alpine

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD [ "python", "./echo-mq.py" ]

# HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8010/actuator/health || exit 1

# EXPOSE 8010
