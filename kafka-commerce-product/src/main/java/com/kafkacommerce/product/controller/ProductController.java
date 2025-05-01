package com.kafkacommerce.product.controller;

import com.kafkacommerce.common.response.ApiResponse;
import com.kafkacommerce.product.dto.ProductCreateRequest;
import com.kafkacommerce.product.dto.ProductResponse;
import com.kafkacommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Validated @RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(response, "상품 등록 성공"));
    }
}