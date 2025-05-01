package com.kafkacommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank
    private String name;
    @NotNull
    private Long price;
    private String description;
    @NotNull
    private Long categoryId;
    private String thumbnailUrl;
} 