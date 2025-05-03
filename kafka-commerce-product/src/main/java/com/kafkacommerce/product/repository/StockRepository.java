package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
} 