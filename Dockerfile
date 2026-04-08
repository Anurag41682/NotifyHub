FROM maven:3.9-eclipse-temurin-17 
COPY . .
RUN mvn clean package -DskipTests
CMD ["java", "-jar", "target/notifyhub-0.0.1-SNAPSHOT.jar"]