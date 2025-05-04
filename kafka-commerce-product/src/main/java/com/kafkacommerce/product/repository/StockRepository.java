package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findStockByProductIdAndOptionId(Long productId, Long optionId);
} 