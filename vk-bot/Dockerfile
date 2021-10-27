FROM openjdk:8-alpine

ADD ./target/vk-bot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/vk-bot.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8040/actuator/health || exit 1

EXPOSE 8040
