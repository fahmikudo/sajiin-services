FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

# Copy parent pom for dependency resolution
COPY pom.xml ./

RUN apk add --no-cache maven && \
    mvn -f pom.xml -N install dependency:go-offline

COPY src ./src

RUN mvn -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8600

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD curl -fsS http://localhost:8600/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
