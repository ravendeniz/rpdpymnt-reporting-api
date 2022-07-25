FROM adoptopenjdk/openjdk8:ubi

# Add Maintainer Info
LABEL maintainer="cs.denizkarakaya@gmail.com"

WORKDIR /opt/fujibas-server/

COPY build/libs/server-*.jar reporting.jar

EXPOSE 8080

CMD java $JVM_ARGS -jar reporting.jar
