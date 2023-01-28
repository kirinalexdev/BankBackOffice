
FROM openjdk:17-oracle

COPY target/bankbackoffice.jar  bankbackoffice.jar

EXPOSE 8080

CMD ["java", "-jar", "/bankbackoffice.jar"]
