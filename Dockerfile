FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY impl/target /app
COPY impl/src/main/resources /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","social.network-impl-1.0.0-SNAPSHOT.jar"]

