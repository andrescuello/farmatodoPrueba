
# Etapa 1: Construcción
FROM gradle:8.10-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean bootJar -x test --no-daemon

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
