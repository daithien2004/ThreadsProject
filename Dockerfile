FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y maven
COPY threads /threads
WORKDIR /threads
RUN mvn clean install -DskipTests
CMD ["java", "-Dserver.port=$PORT", "-jar", "target/threads-0.0.1-SNAPSHOT.jar"]
