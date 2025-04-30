-- 개발 환경 (dev) 스키마 및 사용자 생성
CREATE DATABASE IF NOT EXISTS kafka_commerce_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'kafka_commerce_dev'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'kafka_commerce_dev'@'%' IDENTIFIED BY '1234';

GRANT ALL PRIVILEGES ON kafka_commerce_dev.* TO 'kafka_commerce_dev'@'localhost';
GRANT ALL PRIVILEGES ON kafka_commerce_dev.* TO 'kafka_commerce_dev'@'%';

-- 프로덕션 환경 (prod) 스키마 및 사용자 생성
CREATE DATABASE IF NOT EXISTS kafka_commerce_prod CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'kafka_commerce_prod'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'kafka_commerce_prod'@'%' IDENTIFIED BY '1234';

GRANT ALL PRIVILEGES ON kafka_commerce_prod.* TO 'kafka_commerce_prod'@'localhost';
GRANT ALL PRIVILEGES ON kafka_commerce_prod.* TO 'kafka_commerce_prod'@'%';

-- 권한 적용
FLUSH PRIVILEGES; 