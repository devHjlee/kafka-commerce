package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
} 