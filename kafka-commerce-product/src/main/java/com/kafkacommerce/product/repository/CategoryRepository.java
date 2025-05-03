package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
} 