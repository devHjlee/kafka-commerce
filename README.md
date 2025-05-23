# kafka-commerce 🛒

**Kafka 기반 비동기 이벤트 커머스 시스템**  
Spring Boot 기반 멀티모듈 구조로 사용자, 상품, 주문, 결제 도메인을 분리하여 구현하고,  
Kafka를 활용한 이벤트 기반 주문 처리를 적용한 프로젝트입니다.

---

## 🧩 프로젝트 개요

- **목표**: Kafka 기반의 실시간 주문 처리 시스템 설계 및 구현
- **특징**:
  - 멀티모듈 구조로 도메인 책임 분리
  - Kafka 기반 비동기 이벤트 처리 (Outbox)
  - JWT + Redis 기반 사용자 인증 및 권한 관리
  - 실무형 예외 응답, 공통 응답 포맷 설계

---

## 🔧 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Build Tool**: Gradle (멀티모듈)
- **Messaging**: Kafka
- **Database**: MySQL (개발/프로덕션), H2 (테스트)
- **Security**: Spring Security + JWT
- **Cache**: Redis (리프레시 토큰 관리)
- **Test**: JUnit5

---

## 📦 멀티모듈 구성

- `kafka-commerce-common`  
  → 공통 응답 포맷, 예외 처리, 상수, DTO, JWT
- `kafka-commerce-user`  
  → 회원가입, 로그인, JWT 인증, 리프레시 토큰 관리
- `kafka-commerce-product`  
  → 상품 등록, 재고 관리(redisson 분산락)
- `kafka-commerce-order`  
  → 주문 생성, Kafka 이벤트 발행

---

## 🧱 주요 기능

### ✅ 회원

- 회원가입, 로그인
- JWT 발급 (액세스 토큰, 리프레시 토큰)
- Redis 기반 리프레시 토큰 관리

### ✅ 상품

- 상품 등록
- 재고(Stock) 관리 (상품별 수량)


### ✅ 주문

- 주문 생성
- Kafka 주문 이벤트 발행


---

## 📩 Kafka 이벤트 설계

- **OrderCreatedEvent**  
  `order` → `product`, `payment`  
  → 주문 생성 시 발행

- **PaymentCompletedEvent**  
  `payment` → `order`  
  → 결제 성공 시 주문 상태 변경

- **PaymentFailedEvent**  
  `payment` → `order`  
  → 결제 실패 시 주문 실패 처리

---

## 🔐 보안 설계

### JWT 토큰 관리
- 액세스 토큰: JWT
- 리프레시 토큰: Redis 저장
- 토큰 갱신 프로세스
- 환경별 보안 설정 (개발/테스트/프로덕션)

### 데이터베이스 보안
- 환경별 데이터베이스 설정
- Flyway 마이그레이션
- 사용자 권한 관리

---

## 🛠 개발 환경

### 데이터베이스
- 개발: MySQL (kafka_commerce_dev)
- 테스트: H2
- 프로덕션: MySQL (kafka_commerce_prod)

### Redis
- 리프레시 토큰 저장
- 토큰 만료 관리

### Kafka
- 로컬 개발 환경 설정
- 토픽 구성
- 파티션 전략

---

## 📝 API 문서

### 공통 응답 형식
```json
{
  "code": "SUCCESS",
  "message": "성공",
  "data": {}
}
```

### 주요 API 엔드포인트
- User: /api/auth/*, /api/users/*
  - GET    /api/users/check-nickname?nickname=xxx   # 닉네임 중복 체크
  - GET    /api/users/me                            # 내 정보 조회
- Product: /api/products/*
  - POST   /api/products                # 상품 등록
- Order: /api/orders/*

---

## 🔍 테스트 전략

### 단위 테스트
- 각 모듈의 핵심 비즈니스 로직
- 서비스 레이어
- 유틸리티 클래스

### 통합 테스트
- API 엔드포인트
- Kafka 이벤트 처리
- 데이터베이스 연동

---

---
