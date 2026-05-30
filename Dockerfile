FROM amazoncorretto:25

ARG JAR

COPY $JAR /opt/app.jar

USER 10001

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
