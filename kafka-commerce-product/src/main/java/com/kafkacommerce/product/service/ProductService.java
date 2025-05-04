package com.kafkacommerce.product.service;

import com.kafkacommerce.product.domain.Product;
import com.kafkacommerce.product.domain.ProductStatus;
import com.kafkacommerce.product.dto.request.ProductCreateRequest;
import com.kafkacommerce.product.dto.response.ProductResponse;
import com.kafkacommerce.product.repository.CategoryRepository;
import com.kafkacommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(String name, Long categoryId, String status, Pageable pageable) {
        return productRepository.searchProducts(name, categoryId, status, pageable);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategoryId())
                .status(product.getStatus().name())
                .thumbnailUrl(product.getThumbnailUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductCreateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.update(
                request.getName(),
                request.getPrice(),
                request.getDescription(),
                request.getCategoryId(),
                ProductStatus.AVAILABLE, // 상태 변경은 별도 처리
                request.getThumbnailUrl()
        );
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategoryId())
                .status(product.getStatus().name())
                .thumbnailUrl(product.getThumbnailUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.changeStatus(ProductStatus.HIDDEN); // 소프트 삭제
    }
} 