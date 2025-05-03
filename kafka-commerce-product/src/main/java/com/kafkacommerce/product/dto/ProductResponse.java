package com.kafkacommerce.product.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Long categoryId;
    private String status;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
} 