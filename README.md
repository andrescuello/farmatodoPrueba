
# Farmatodo Challenge (Java 17 + Spring Boot 3)

Reto tecnico - Desarrollador Java

## Correr en local
```bash
./gradlew clean bootJar 
# o en Windows: gradlew.bat bootRun
```

La app levanta en `http://localhost:8080/ping` (no requiere API-Key para `/ping`).

## Config por defecto
- base de datos en PostgreSQL 
- `schema-postgresql.sql` se ejecuta en el arranque
- `data-postgresql.sql` se ejecuta en el arranque

## API-Key
Rutas distintas de `/ping` requieren header:
```
X-API-KEY: dev-123456
```
(se ajusta en `application.yml`)

## Estructura
- `domain/` entidades JPA (UUID + timestamps manejados por BD)
- `repo/` repositorios Spring Data JPA
- `config/` filtro de API-Key y security
- `resources/schema-postgresql.sql` esquema base

## Tests & Coverage
```bash
./gradlew clean test jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```
La verificación de cobertura falla si es < 80% (configurado en `build.gradle`).

## Correr con Docker
```bash
./gradlew clean bootJar
docker compose build --no-cache app 
docker compose up -d
docker compose logs -f app
```


## Docker (producción, no-root)
```bash
./gradlew clean bootJar
docker build -f Dockerfile.prod -t farmatodo-app:prod .
docker run --rm -p 8080:8080 farmatodo-app:prod
```
