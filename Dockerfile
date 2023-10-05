FROM khipu/openjdk17-alpine
COPY impl/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

