
# 빌드 단계
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN  ./gradlew clean build -x test

# 실행 단계
FROM openjdk:17-jdk-slim

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=deploy

ENV_FILE=./.env

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# deploy로 프로필을 설정했다. application.yml파일을 사용