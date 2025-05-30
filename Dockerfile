# Fase de construcción
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

# Copia los archivos necesarios para construir
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Construye la aplicación (ajusta si usas Gradle)
RUN ./mvnw clean package -DskipTests

# Fase de ejecución
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia el JAR desde la fase de construcción
COPY --from=builder /app/target/*.jar app.jar

# Configuración adicional
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
