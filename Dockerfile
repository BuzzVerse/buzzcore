#Build stage
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /usr/app/
COPY . .
RUN gradle build

# Package stage
FROM openjdk:21-jdk

ENV JAR_NAME=app.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .

# Rename the JAR file that does not contain 'plain'
RUN mv $(ls /usr/app/build/libs/*SNAPSHOT.jar | grep -v "plain") /usr/app/build/libs/$JAR_NAME

ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME