package com.kafkacommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequest {
    @NotBlank
    private String name;
    private Long parentId; // 대분류는 null
    private Integer sortOrder;
} 