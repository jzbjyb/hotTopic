FROM localhost:5000/java:7

VOLUME /var/log/hottopic
ADD hottopic-rest-service-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
