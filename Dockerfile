FROM maven:3.9.6-eclipse-temurin-21-jammy as build

WORKDIR "/app"
COPY . .

RUN mvn package -ntp
 
FROM eclipse-temurin:21-jre-jammy

COPY --from=build /app/target/*.jar /app/app.jar 

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]  