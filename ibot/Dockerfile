FROM openjdk:8-alpine

ADD ./target/ibot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/ibot.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8020/actuator/health || exit 1

EXPOSE 8020
