FROM amazoncorretto:25

ARG JAR

COPY $JAR /opt/app.jar

ENV SERVER_SERVLET_CONTEXT_PATH="/api"

USER 10001

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
