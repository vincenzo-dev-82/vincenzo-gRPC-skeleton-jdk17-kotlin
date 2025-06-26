# Spring Boot 3 + Kotlin gRPC Skeleton Project

## 🚀 프로젝트 개요

이 프로젝트는 Spring Boot 3와 JDK 17 기반에서 Kotlin을 사용하여 구현한 gRPC 샘플 애플리케이션입니다.

## 📚 gRPC란?

gRPC(gRPC Remote Procedure Calls)는 Google에서 개발한 오픈소스 고성능 RPC(Remote Procedure Call) 프레임워크입니다. HTTP/2를 기반으로 하며, Protocol Buffers를 인터페이스 정의 언어(IDL)로 사용합니다.

### 주요 특징

1. **HTTP/2 기반**
   - 멀티플렉싱: 단일 TCP 연결로 여러 요청을 동시에 처리
   - 헤더 압축: 네트워크 오버헤드 감소
   - 서버 푸시: 클라이언트 요청 없이 서버가 리소스를 미리 전송

2. **Protocol Buffers (ProtoBuf)**
   - 언어 중립적이고 플랫폼 중립적인 직렬화 메커니즘
   - JSON보다 3-10배 작고, 20-100배 빠른 직렬화/역직렬화
   - 스키마 진화(Schema Evolution) 지원

3. **스트리밍 지원**
   - Unary RPC: 단일 요청-단일 응답
   - Server Streaming: 단일 요청-다중 응답
   - Client Streaming: 다중 요청-단일 응답
   - Bidirectional Streaming: 양방향 스트리밍

## 🎯 gRPC 사용 시나리오

### 적합한 경우

1. **마이크로서비스 간 통신**
   - 내부 서비스 간 고성능 통신이 필요할 때
   - 엄격한 API 계약이 필요한 경우
   - 다양한 언어로 구현된 서비스 간 통신

2. **실시간 통신**
   - 실시간 데이터 스트리밍
   - 채팅 서비스, 실시간 업데이트
   - IoT 디바이스 통신

3. **모바일 애플리케이션**
   - 배터리 효율적인 통신이 필요한 경우
   - 네트워크 대역폭이 제한적인 환경

### 부적합한 경우

1. **브라우저 클라이언트**
   - 표준 브라우저는 HTTP/2의 gRPC 구현을 직접 지원하지 않음
   - gRPC-Web을 사용하거나 REST API 게이트웨이 필요

2. **퍼블릭 API**
   - JSON/REST가 더 널리 사용되고 이해하기 쉬움
   - 디버깅과 테스트가 상대적으로 어려움

## ⚖️ 장단점

### 장점

1. **성능**
   - Binary 프로토콜로 JSON보다 빠른 직렬화/역직렬화
   - HTTP/2의 멀티플렉싱으로 연결 오버헤드 감소
   - 헤더 압축으로 네트워크 사용량 감소

2. **타입 안전성**
   - 강력한 타입 시스템
   - 컴파일 타임에 오류 검출
   - 자동 코드 생성으로 실수 방지

3. **다중 언어 지원**
   - 다양한 프로그래밍 언어 지원
   - 언어 간 상호 운용성 보장

4. **스트리밍**
   - 실시간 양방향 통신 지원
   - 대용량 데이터 전송에 효율적

### 단점

1. **복잡성**
   - 초기 설정이 REST보다 복잡
   - Protocol Buffers 학습 필요
   - 디버깅이 상대적으로 어려움

2. **브라우저 지원**
   - 네이티브 브라우저 지원 부족
   - gRPC-Web 프록시 필요

3. **가시성**
   - Binary 프로토콜로 사람이 읽기 어려움
   - 특별한 도구 없이 디버깅 어려움

## 🛠️ 프로젝트 구조

```
vincenzo-gRPC-skeleton-jdk17-kotlin/
├── build.gradle.kts          # Gradle 빌드 설정
├── settings.gradle.kts       # 프로젝트 설정
├── gradle.properties         # Gradle 속성
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/example/grpc/
│   │   │       ├── GrpcApplication.kt      # 메인 애플리케이션
│   │   │       ├── service/
│   │   │       │   └── GreeterService.kt   # gRPC 서비스 구현
│   │   │       └── client/
│   │   │           └── GreeterClient.kt    # gRPC 클라이언트
│   │   ├── proto/
│   │   │   └── greeter.proto              # Protocol Buffer 정의
│   │   └── resources/
│   │       └── application.yml            # 애플리케이션 설정
│   └── test/
│       └── kotlin/
│           └── com/example/grpc/
│               └── GreeterServiceTest.kt  # 테스트 코드
└── README.md
```

## 🚀 시작하기

### 필요 사항

- JDK 17 이상
- Gradle 7.x 이상

### 빌드 및 실행

1. **프로젝트 클론**
   ```bash
   git clone https://github.com/vincenzo-dev-82/vincenzo-gRPC-skeleton-jdk17-kotlin.git
   cd vincenzo-gRPC-skeleton-jdk17-kotlin
   ```

2. **빌드**
   ```bash
   ./gradlew build
   ```

3. **서버 실행**
   ```bash
   ./gradlew bootRun
   ```

4. **클라이언트 테스트**
   ```bash
   ./gradlew run --args="client"
   ```

### gRPC 서버 엔드포인트

- **gRPC Server**: `localhost:9090`
- **Spring Boot Actuator**: `http://localhost:8080/actuator`

## 📝 예제 코드

### Proto 파일 정의

```protobuf
syntax = "proto3";

service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
  rpc SayHelloStream (HelloRequest) returns (stream HelloReply) {}
}

message HelloRequest {
  string name = 1;
}

message HelloReply {
  string message = 1;
}
```

### 서비스 구현

```kotlin
@GrpcService
class GreeterService : GreeterGrpc.GreeterImplBase() {
    
    override fun sayHello(
        request: HelloRequest,
        responseObserver: StreamObserver<HelloReply>
    ) {
        val reply = HelloReply.newBuilder()
            .setMessage("Hello ${request.name}")
            .build()
            
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
}
```

## 🔧 설정

### application.yml

```yaml
grpc:
  server:
    port: 9090

spring:
  application:
    name: grpc-skeleton

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

## 📊 모니터링

- Health Check: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- gRPC 서비스 상태는 Spring Boot Actuator와 통합되어 확인 가능

## 🤝 기여하기

이슈 및 PR은 언제든 환영합니다!

## 📄 라이센스

MIT License
