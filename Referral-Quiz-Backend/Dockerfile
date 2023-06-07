FROM maven:3.8.6-openjdk-18 as builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn package -DskipTests

#### Stage 2:
FROM openjdk:18-alpine

WORKDIR /app

COPY src/main/resources/db/migration /app/db/migration

COPY --from=builder /app/target/referral-quiz-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]