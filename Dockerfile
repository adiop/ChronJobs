FROM openjdk:8

ENV JAVA_OPTS="-Dspring.profiles.active=prod -Xmx4096m -Xms4096m -XX:MaxPermSize=256m"

ADD build/libs/ChronJobs-0.0.1-SNAPSHOT.jar ChronJobs-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ChronJobs-0.0.1-SNAPSHOT.jar"]
