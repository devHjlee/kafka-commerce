# kafka-commerce 🛒

**Kafka 기반 비동기 이벤트 커머스 시스템**  
Spring Boot 기반 멀티모듈 구조로 사용자, 상품, 주문, 결제 도메인을 분리하여 구현하고,  
Kafka를 활용한 이벤트 기반 주문 처리를 적용한 프로젝트입니다.

---

## 🧩 프로젝트 개요

- **목표**: Kafka 기반의 실시간 주문 처리 시스템 설계 및 구현
- **특징**:
  - 멀티모듈 구조로 도메인 책임 분리
  - Kafka 기반 비동기 이벤트 처리
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
  → 공통 응답 포맷, 예외 처리, 상수, DTO
- `kafka-commerce-user`  
  → 회원가입, 로그인, JWT 인증, 리프레시 토큰 관리
- `kafka-commerce-product`  
  → 상품 등록, 조회, 재고 관리 (재고 모듈 통합 여부 미정)
- `kafka-commerce-order`  
  → 주문 생성, Kafka 이벤트 발행
- `kafka-commerce-payment`  
  → 결제 처리 (카드, 현금, 마일리지), 이벤트 발행

---

## 🧱 주요 기능

### ✅ 회원

- 회원가입, 로그인
- JWT 발급 (액세스 토큰, 리프레시 토큰)
- Redis 기반 리프레시 토큰 관리
- 닉네임 중복 체크
- 내 정보 조회/수정
- 비밀번호 변경

### ✅ 상품

- 상품 등록 (관리자)
- 상품 목록/상세 조회
- 재고 관리 (재고 모듈 통합 여부 미정)

### ✅ 주문

- 주문 생성
- Kafka 주문 이벤트 발행
- 내 주문 목록 조회

### ✅ 결제

- 결제 처리 (카드, 현금, 마일리지)
- 실제 PG 연동 없음
- 결제 상태 관리
- Kafka 결제 이벤트 발행

### ✅ 배송지

- 배송지 목록 조회
- 배송지 등록
- 배송지 수정
- 배송지 삭제
- 기본 배송지 설정

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
  - PUT    /api/users/me                            # 내 정보 수정
  - PUT    /api/users/me/password                   # 비밀번호 변경
- Product: /api/products/*
- Order: /api/orders/*
- Payment: /api/payments/*
- DeliveryAddress: /api/delivery-addresses/*
  - GET    /api/delivery-addresses                 # 내 배송지 목록 조회
  - POST   /api/delivery-addresses                 # 배송지 등록
  - PUT    /api/delivery-addresses/{id}            # 배송지 수정
  - DELETE /api/delivery-addresses/{id}            # 배송지 삭제
  - PATCH  /api/delivery-addresses/{id}/default    # 기본 배송지 설정

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

## 🚀 배포 전략

### 환경 구성
- 개발 환경 (로컬)
- 테스트 환경
- 프로덕션 환경

### CI/CD
- GitHub Actions를 통한 자동화
- 환경별 배포 파이프라인

---

## 📊 모니터링

### 로깅
- 로그 레벨 설정
- 로그 포맷
- 로그 수집 전략

### 메트릭
- API 응답 시간
- Kafka 메시지 처리량
- 데이터베이스 성능

---

## 🧑‍💻 로컬 실행 방법

```bash
# Kafka, Zookeeper, MySQL 실행 필요
$ ./gradlew clean build
$ cd kafka-commerce-user
$ ./gradlew bootRun
```

---
