# 1단계: Gradle 환경에서 프로젝트 빌드
FROM gradle:8.5-jdk17 AS build

WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .

# gradlew 실행 권한 부여 및 빌드
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# 2단계: 최신 Java 17 실행 환경
FROM eclipse-temurin:17-jre-alpine

EXPOSE 8080

# 빌드된 jar 파일 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
