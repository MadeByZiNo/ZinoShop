FROM openjdk:21-jdk-slim
COPY app.jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
