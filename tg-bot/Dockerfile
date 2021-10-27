FROM openjdk:8-alpine

ADD ./target/tg-bot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/tg-bot.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8030/actuator/health || exit 1

EXPOSE 8030
