FROM openjdk:8

ENV JAVA_OPTS="-Xmx4096m -Xms4096m -XX:MaxPermSize=256m"

ADD build/libs/Chronjobs-0.0.1-SNAPSHOT.jar Chronjobs.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=staging", "-jar", "Chronjobs.jar"]