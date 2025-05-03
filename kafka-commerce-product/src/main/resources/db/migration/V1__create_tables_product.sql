-- 상품 테이블 생성
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 상품 고유 ID',
    name VARCHAR(200) NOT NULL COMMENT '상품명',
    price BIGINT NOT NULL COMMENT '가격',
    description VARCHAR(2000) COMMENT '상품 설명',
    category_id BIGINT NOT NULL COMMENT '카테고리 ID',
    status VARCHAR(20) NOT NULL COMMENT '상품 상태(AVAILABLE/SOLD_OUT/HIDDEN)',
    thumbnail_url VARCHAR(500) COMMENT '썸네일 이미지 URL',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    INDEX idx_product_category (category_id),
    INDEX idx_product_status (status)
) COMMENT='상품 정보 테이블';

-- 카테고리 테이블 생성
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 카테고리 고유 ID',
    name VARCHAR(100) NOT NULL COMMENT '카테고리명',
    parent_id BIGINT COMMENT '상위 카테고리 ID(대분류는 NULL)',
    sort_order INT COMMENT '정렬 순서',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    INDEX idx_category_parent (parent_id)
) COMMENT='상품 카테고리 테이블';

-- 상품 옵션 테이블 생성
CREATE TABLE product_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 옵션 고유 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID',
    option_name VARCHAR(100) COMMENT '옵션명(예: 사이즈)',
    option_value VARCHAR(100) COMMENT '옵션값(예: M)',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    INDEX idx_option_product (product_id)
) COMMENT='상품 옵션 테이블';

-- 재고 테이블 생성
CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 재고 고유 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID',
    option_id BIGINT NOT NULL COMMENT '옵션 ID(옵션 없는 상품은 단일 옵션 ID)',
    quantity INT NOT NULL COMMENT '재고 수량',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    INDEX idx_stock_product (product_id),
    INDEX idx_stock_option (option_id)
) COMMENT='상품 재고 테이블';

-- 상품 좋아요 테이블 생성
CREATE TABLE product_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 좋아요 고유 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID',
    user_id BIGINT NOT NULL COMMENT '유저 ID',
    created_at DATETIME NOT NULL COMMENT '좋아요 등록일시',
    UNIQUE KEY uk_product_like (product_id, user_id),
    INDEX idx_like_product (product_id),
    INDEX idx_like_user (user_id)
) COMMENT='상품 좋아요(찜) 테이블';

-- 상품 이미지 테이블 생성
CREATE TABLE product_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK: 이미지 고유 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID',
    image_url VARCHAR(500) NOT NULL COMMENT '이미지 URL',
    is_thumbnail BOOLEAN NOT NULL DEFAULT FALSE COMMENT '썸네일 여부',
    created_at DATETIME NOT NULL COMMENT '생성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    INDEX idx_image_product (product_id)
) COMMENT='상품 이미지 테이블'; 