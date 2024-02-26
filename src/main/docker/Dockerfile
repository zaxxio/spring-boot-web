FROM maven:3.8.1-openjdk-17
#COPY ./../../../src src
#COPY ./../../../pom.xml pom.xml
#RUN mvn clean package
COPY ./../../../target/app.jar app.jar
EXPOSE 8443
#CMD ["java", "-jar", "target/app.jar"]
CMD ["java", "-jar", "app.jar"]
