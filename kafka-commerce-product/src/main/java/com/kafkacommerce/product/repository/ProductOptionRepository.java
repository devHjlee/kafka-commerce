package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
} 