FROM adoptopenjdk:11-jre-hotspot
COPY impl/target /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]