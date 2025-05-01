-- users 테이블 생성
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 사용자 고유 ID',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일(로그인 ID)',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호(암호화 저장)',
    name VARCHAR(255) NOT NULL COMMENT '사용자 이름',
    role VARCHAR(20) NOT NULL COMMENT '권한(예: USER, ADMIN)',
    nickname VARCHAR(255) COMMENT '닉네임',
    phone_number VARCHAR(50) COMMENT '휴대폰 번호',
    profile_url VARCHAR(500) COMMENT '프로필 이미지 URL',
    status VARCHAR(20) NOT NULL COMMENT '계정 상태(ACTIVE/INACTIVE/DELETED)',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    last_login_at DATETIME COMMENT '마지막 로그인 일시',
    INDEX idx_users_status (status)
) COMMENT='회원 정보 테이블'; 