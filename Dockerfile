# Étape de construction
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copier le fichier pom.xml pour télécharger les dépendances
COPY pom.xml .

# Télécharger les dépendances (cela est séparé pour optimiser le cache des dépendances)
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src src

# Construire l'application
RUN mvn clean package -DskipTests

# Étape finale avec l'image Alpine OpenJDK 17
FROM adoptopenjdk:17-jre-hotspot-bionic as final

WORKDIR /app

# Copier le JAR construit à partir de l'étape de construction précédente
COPY --from=build /app/target/*.jar /app/sambaApi.jar

# Exposer le port sur lequel l'application s'exécute
EXPOSE 9000

# Définir la commande pour exécuter l'application
CMD ["java", "-jar", "sambaApi.jar"]
