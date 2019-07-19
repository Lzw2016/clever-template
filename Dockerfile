FROM 172.18.1.1:15000/java:8u111-jre-alpine as dev
ADD clever-template-server/target/clever-template-server-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0"]
EXPOSE 9066

FROM 172.18.1.1:15000/java:8u111-jre-alpine as prod
ADD clever-template-server/target/clever-template-server-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0"]
EXPOSE 9066
