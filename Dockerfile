# docker build -t ordoghunor/ix-backend:0.2.5 .
# docker push ordoghunor/ix-backend:0.2.5

FROM openjdk:17

WORKDIR /app

COPY build/libs/ix.jar /app/ix.jar

EXPOSE 8080

CMD ["java", "-jar", "ix.jar"]