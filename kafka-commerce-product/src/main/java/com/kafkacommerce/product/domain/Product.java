package com.kafkacommerce.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.kafkacommerce.common.entity.BaseEntity;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Long categoryId; // 연관관계 없이 ID만 저장

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(length = 500)
    private String thumbnailUrl;

    @Builder
    private Product(String name, Long price, String description, Long categoryId, ProductStatus status, String thumbnailUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.status = status != null ? status : ProductStatus.AVAILABLE;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void update(String name, Long price, String description, Long categoryId, ProductStatus status, String thumbnailUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.status = status;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }
} 