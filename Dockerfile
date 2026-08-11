# ==========================================
# 1단계: Gradle 환경에서 프로젝트 빌드 (.jar 생성)
# ==========================================
FROM gradle:7.6-jdk17 AS build

# 소스 코드를 컨테이너 내부로 복사
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# 테스트를 제외하고 .jar 파일 빌드
RUN ./gradlew build -x test --no-daemon

# ==========================================
# 2단계: 경량화된 Java 환경에서 실행
# ==========================================
FROM openjdk:17-jdk-slim

# 컨테이너 포트 설정 (Spring Boot 기본 포트)
EXPOSE 8080

# 1단계에서 빌드된 .jar 파일만 추출하여 복사
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
