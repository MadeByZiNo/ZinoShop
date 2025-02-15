FROM openjdk:21-jdk-slim
COPY target/zinoshop.jar /app/zinoshop.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/zinoshop.jar"]
