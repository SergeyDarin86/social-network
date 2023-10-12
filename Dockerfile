FROM khipu/openjdk17-alpine
WORKDIR /app
COPY impl/target/social.network-impl-1.0.0-SNAPSHOT.jar /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","social.network-impl-1.0.0-SNAPSHOT.jar"]


