FROM amazoncorretto:11.0.19

WORKDIR /usr/src/api

COPY target/Leiturando-0.0.1-SNAPSHOT.jar /usr/src/api/Leiturando.jar

EXPOSE 8080

CMD ["java", "-jar", "Leiturando.jar"]
