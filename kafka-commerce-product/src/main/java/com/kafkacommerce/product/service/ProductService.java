package com.kafkacommerce.product.service;

import com.kafkacommerce.product.domain.Product;
import com.kafkacommerce.product.domain.ProductStatus;
import com.kafkacommerce.product.dto.ProductCreateRequest;
import com.kafkacommerce.product.dto.ProductResponse;
import com.kafkacommerce.product.repository.CategoryRepository;
import com.kafkacommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // 카테고리 존재 여부 검증
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .status(ProductStatus.AVAILABLE)
                .thumbnailUrl(request.getThumbnailUrl())
                .build();
        Product saved = productRepository.save(product);
        return ProductResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .description(saved.getDescription())
                .categoryId(saved.getCategoryId())
                .status(saved.getStatus().name())
                .thumbnailUrl(saved.getThumbnailUrl())
                .createdAt(saved.getCreatedAt())
                .build();
    }
} 