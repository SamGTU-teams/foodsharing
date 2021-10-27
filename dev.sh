. ./env.sh

sh ./mvnw install -DskipTests

docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build
