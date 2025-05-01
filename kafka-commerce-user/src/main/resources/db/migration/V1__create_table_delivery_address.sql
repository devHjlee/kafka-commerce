-- delivery_address 테이블 생성
CREATE TABLE delivery_address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 배송지 고유 ID',
    user_id BIGINT NOT NULL COMMENT '회원 ID (users.id)',
    receiver_name VARCHAR(50) NOT NULL COMMENT '수령인 이름',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '수령인 연락처',
    zipcode VARCHAR(10) NOT NULL COMMENT '우편번호',
    address1 VARCHAR(200) NOT NULL COMMENT '기본 주소',
    address2 VARCHAR(200) COMMENT '상세 주소',
    is_default BOOLEAN NOT NULL COMMENT '기본 배송지 여부',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    INDEX idx_delivery_address_user_id (user_id),
    INDEX idx_delivery_address_user_id_is_default (user_id, is_default),
    INDEX idx_delivery_address_user_id_created_at (user_id, created_at)
    -- 필요시 외래키 제약조건 추가
    -- ,FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT='회원 배송지 정보 테이블'; 