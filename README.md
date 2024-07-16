# Wird Platform Backend

Este proyecto es un servicio de backend en Kotlin utilizando Ktor que consulta una API meteorológica para varias localidades y almacena los resultados en Redis. El servicio consulta la API cada 5 minutos y permite acceder a los datos almacenados a través de una API REST. Implementa una lógica de reintento en caso de fallo con una probabilidad del 20%.

## Localidades Monitoreadas

- Santiago (CL)
- Zúrich (CH)
- Auckland (NZ)
- Sídney (AU)
- Londres (UK)
- Georgia (USA)

## Tecnologías Utilizadas

- Kotlin
- Ktor
- Redis (Lettuce como cliente Redis)
- Coroutines
- Ktor Client
- Logback
- Docker

## Requisitos Previos

- [Java 11+](https://adoptopenjdk.net/)
- [Kotlin](https://kotlinlang.org/)
- [Redis](https://redis.io/)
- [Gradle](https://gradle.org/)
- [Docker](https://www.docker.com/)

## Configuración del Proyecto

### 1. Clonar el Repositorio

```sh
git clone https://github.com/ruzzll/wird-platform-backend.git
cd wird-platform-backend
```

### 2. Levantar el proyecto
El proyecto incluye un archivo docker-compose.yml en el directorio raíz. Asegúrate de tener Docker y Docker Compose instalados. Para iniciar Redis, ejecuta el siguiente comando:

```sh
docker-compose up
```

### 3. Uso de la API REST
Endpoint

```
GET http://localhost:8080/weather/{location}
```

### 4. Manejo de Errores
- Si una llamada a la API meteorológica falla (simulación de fallo del 20%), se intentará hasta un máximo de 3 veces con un retraso de 2 segundos entre cada intento.
- Si todos los intentos fallan, se registrará el error en Redis con un timestamp.

### 5. Registro de Errores
Los errores se almacenan en una lista en Redis. Cada error incluye el mensaje de error y el timestamp.

### 4. Contribuciones
Las contribuciones son bienvenidas. Por favor, abre un issue o un pull request para sugerir mejoras.

