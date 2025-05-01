package com.kafkacommerce.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.kafkacommerce.common.entity.BaseEntity;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isThumbnail;

    @Builder
    private ProductImage(Long productId, String imageUrl, Boolean isThumbnail) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail != null ? isThumbnail : false;
    }

    public void update(String imageUrl, Boolean isThumbnail) {
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
    }
} 