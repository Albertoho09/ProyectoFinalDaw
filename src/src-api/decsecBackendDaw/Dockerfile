FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/decsecBackend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} decsec_api.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "decsec_api.jar"]