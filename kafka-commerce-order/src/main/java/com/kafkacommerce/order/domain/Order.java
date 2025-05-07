package com.kafkacommerce.order.domain;

import com.kafkacommerce.common.entity.BaseEntity;
import com.kafkacommerce.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long optionId;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}

