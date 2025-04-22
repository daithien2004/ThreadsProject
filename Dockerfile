FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y maven

WORKDIR /threads

COPY . /threads

RUN mvn clean install

CMD ["java", "-Dserver.port=$PORT", "-jar", "threads/target/threads-0.0.1-SNAPSHOT.jar"]
