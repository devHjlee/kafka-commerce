package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.domain.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
} 