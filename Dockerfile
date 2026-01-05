FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline

COPY src src
RUN ./mvnw -q -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 9100

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD curl -fsS http://localhost:8600/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
