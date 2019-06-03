FROM maven:3.6-slim

ADD src/ src
ADD pom.xml pom.xml

RUN mvn clean package


FROM openjdk:8
COPY --from=0 target/openidclient-0.0.1-SNAPSHOT.jar app.jar
ADD cacerts cacerts

ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=/cacerts", "-jar", "app.jar"]

CMD []