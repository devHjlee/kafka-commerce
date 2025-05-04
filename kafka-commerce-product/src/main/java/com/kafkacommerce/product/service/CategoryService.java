package com.kafkacommerce.product.service;

import com.kafkacommerce.product.domain.Category;
import com.kafkacommerce.product.dto.request.CategoryCreateRequest;
import com.kafkacommerce.product.dto.response.CategoryResponse;
import com.kafkacommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .parentId(request.getParentId())
                .sortOrder(request.getSortOrder())
                .build();
        Category saved = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .parentId(saved.getParentId())
                .sortOrder(saved.getSortOrder())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
} 