FROM eclipse-temurin:17

WORKDIR /app

COPY target/springboot-blog-rest-api-0.0.1-SNAPSHOT.jar /app/springboot-blog-app.jar

ENTRYPOINT ["java", "-jar","springboot-blog-app.jar"]