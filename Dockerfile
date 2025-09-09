FROM azul/zulu-openjdk:24

# Crea una directory di lavoro
WORKDIR /app

# Copia il JAR generato nel container
COPY target/*.jar app.jar

# Esponi la porta 8080 (default Spring)
EXPOSE 8080

# Comando per avviare l'app
ENTRYPOINT ["java", "-jar", "app.jar"]