# Usa Java 21 (più stabile) - cambia con openjdk:24-ea-jdk-slim se hai problemi
FROM openjdk:24-ea-jdk-slim

# Directory di lavoro nel container
WORKDIR /app

# Copia il JAR compilato (modifica il nome secondo il tuo progetto)
# Il pattern footbro-*.jar prenderà qualsiasi JAR che inizia con "footbro-"
COPY target/Footbro-*.jar app.jar

# Esponi la porta 8080 (porta standard Spring Boot)
EXPOSE 8080

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]