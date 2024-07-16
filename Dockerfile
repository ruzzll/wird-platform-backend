# Usa una imagen base de Kotlin con OpenJDK 11 para compilar la aplicación
FROM openjdk:11-jdk-slim AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de Gradle necesarios y realiza el build de la aplicación
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew ./
COPY gradle ./gradle
RUN ./gradlew build || return 0

# Copia el código fuente y realiza la compilación
COPY src ./src
RUN ./gradlew build

# En una segunda etapa, usa una imagen más liviana de OpenJDK 11
FROM openjdk:11-jre-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR compilado desde la etapa de construcción anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto expuesto por la aplicación Ktor
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
