FROM maven:3.9.11-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -o

FROM eclipse-temurin:11-jdk
FROM jboss/wildfly:20.0.1.Final
COPY forDockerStandalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml

RUN mkdir -p /opt/jboss/wildfly/modules/org/postgresql/main/
COPY forDockerModule.xml /opt/jboss/wildfly/modules/org/postgresql/main/module.xml
COPY --from=build /root/.m2/repository/org/postgresql/postgresql/*/postgresql-*.jar /opt/jboss/wildfly/modules/org/postgresql/main/postgresql-42.7.7.jar

COPY --from=build /app/target/*.war /opt/jboss/wildfly/standalone/deployments/lab3.war
EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
