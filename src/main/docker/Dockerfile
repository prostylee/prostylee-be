# For Java 8, try this
# FROM openjdk:8-jdk-alpine

# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre

VOLUME /tmp

# Set the working directory
WORKDIR /opt/app

# Copy spring-boot-app.jar to /opt/app/app.jar
ARG JAR_FILE=build/libs/prostylee-be-*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8090

# Run the jar file
# ENTRYPOINT ["java", "${JAVA_OPTS}", "-jar", "app.jar"]
CMD [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_ACTIVE_PROFILE} -jar app.jar" ]