FROM eclipse-temurin:11.0.17_8-jre-jammy
ENV PORT 8080
EXPOSE $PORT

ENV LOGGING_CONFIG_FILE logback-spring.xml

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

WORKDIR /opt

COPY target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -noverify -XX:+AlwaysPreTouch -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom -Dspring.cloud.config.enabled=false -jar app.jar"]
