package com.kafkacommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import com.querydsl.core.annotations.QueryProjection;

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

    @QueryProjection
    public ProductResponse(Long id, String name, Long price, String description, Long categoryId, String status, String thumbnailUrl, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.status = status;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }
} 