# Fase de construcción
FROM eclipse-temurin:17-jdk-jammy as builder

# Configura el directorio de trabajo
WORKDIR /workspace/app

# 1. Primero copia solo los archivos de configuración
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 2. Da permisos de ejecución al mvnw
RUN chmod +x mvnw

# 3. Descarga las dependencias primero (esto crea una capa caché)
RUN ./mvnw dependency:go-offline -B

# 4. Copia el código fuente
COPY src src

# 5. Construye la aplicación
RUN ./mvnw clean package -DskipTests

# Fase de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia el JAR desde la fase de construcción
COPY --from=builder /workspace/app/target/*.jar app.jar

# Expone el puerto y ejecuta la aplicación
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
