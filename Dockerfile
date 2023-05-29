FROM openjdk:17-jdk

WORKDIR /app

COPY target/springboot-blog-rest-api-0.0.1-SNAPSHOT.jar /app/springboot-blog-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","springboot-blog-app.jar"]