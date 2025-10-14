FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jdk
FROM quay.io/wildfly/wildfly:latest-jdk21
COPY --from=build /app/target/*.war /opt/jboss/wildfly/standalone/deployments/
COPY forDockerStandalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
