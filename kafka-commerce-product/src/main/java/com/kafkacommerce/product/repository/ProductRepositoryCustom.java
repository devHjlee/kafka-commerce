package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    // QueryDSL 커스텀 메서드 선언 (예: 상품 검색, 필터 등)
    Page<ProductResponse> searchProducts(String name, Long categoryId, String status, Pageable pageable);
} 