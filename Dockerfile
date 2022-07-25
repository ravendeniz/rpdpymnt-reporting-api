FROM adoptopenjdk/openjdk8:ubi

# Add Maintainer Info
LABEL maintainer="cs.denizkarakaya@gmail.com"

WORKDIR /opt/fujibas-server/

COPY build/libs/server-*.jar reporting-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD java $JVM_ARGS -jar reporting-0.0.1-SNAPSHOT.jar
