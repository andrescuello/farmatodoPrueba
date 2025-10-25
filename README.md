
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


## Despliegie a producción en GCP

Para el desplegue en GCP se procedio a crear la base de datos a partir del archivo schema-postgresql.sql en la plataforma de Cloud SQL,
luego se procedio a crea en Cloud run el proyecto para poder desplegar la microaplicación, despues se procede a conectar el repositorio
de github para poder montar el proyecto. al momento de montar el proyecto se crearon las variables de entornos en el Cloud run para
producción, dichas variables de entorno se ajustaron a las necesidades del proyecto, que a la final permitieron subir el proyecto.

