package com.kafkacommerce.product.controller;

import com.kafkacommerce.common.response.ApiResponse;
import com.kafkacommerce.product.dto.CategoryCreateRequest;
import com.kafkacommerce.product.dto.CategoryResponse;
import com.kafkacommerce.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Validated @RequestBody CategoryCreateRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response, "카테고리 등록 성공"));
    }
}