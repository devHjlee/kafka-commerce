package com.kafkacommerce.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.kafkacommerce.common.entity.BaseEntity;

@Entity
@Table(name = "product_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 100)
    private String optionName; // 예: "사이즈", 옵션 없는 상품은 null

    @Column(length = 100)
    private String optionValue; // 예: "M", 옵션 없는 상품은 null

    @Builder
    private ProductOption(Long productId, String optionName, String optionValue) {
        this.productId = productId;
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    public void update(String optionName, String optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }
} 