# Spring Stream Video

## 실행법

### 1. application.yml 구성

```yml
# src/main/resources/application.yml 생성 후 입력
spring:
  servlet:
    multipart:
      maxFileSize: 1000MB # 파일 하나의 최대 크기
      maxRequestSize: 1000MB  # 한 번에 최대 업로드 가능 용량
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://{DB 호스트 주소}:{DB 포트}/stream?characterEncoding=UTF-8&serverTimezone=UTC
    username: { DB 유저명 }
    password: { DB 유저 비밀번호 }
  jpa:
    database: mysql
    database-platform: org.hibernate.dialect.MySQLDialect
    show-sql: true
    hibernate:
      ddl-auto: update
  flyway:
    enabled: true
    baselineOnMigrate: true
    validateOnMigrate: true
    locations: classpath:db/migration

logging:
  level:
    ROOT: INFO
    com.stream: DEBUG

my:
  app:
    storage:
      type: local
      localStorageDir: ./videos
    jwt:
      signingKey: { JWT 사이닝을 위한 랜덤 문자열 }

```

### 2. 외부 의존성 실행 (ex. DB)

`docker-compose up -d`

### 3. 서버 실행

`./gradlew bootrun`

## EP

- 서버 실행 후 OpenAPI(Swagger)를 통해 확인
- /swagger-ui/index.html

## 인증

- JWT 기반
- 로그인 시 짧은 만료 기간의 액세스 토큰 발급
- 토큰 내에 유저 식별자와 권한(역할 기반) 포함

## 코드 컨벤션

- .editorconfig, checkstyle 활용을 통한 컨벤션 강제
- 참조 (https://naver.github.io/hackday-conventions-java/)

## 비디오 저장소

### 현재 지원

- 로컬 파일시스템

### 향후 지원 계획

- SeaweedFS (로컬 스토리지 서비스)
- Aws S3
