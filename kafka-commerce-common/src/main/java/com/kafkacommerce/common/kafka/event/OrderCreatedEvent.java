package com.kafkacommerce.common.kafka.event;

import com.kafkacommerce.common.enums.OrderStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long productId;
    private Long optionId;
    private int quantity;
    private Long userId;
    private OrderStatus status;

    @Builder
    private OrderCreatedEvent(Long orderId, Long productId, Long optionId, int quantity, Long userId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.userId = userId;
        this.status = orderStatus;
    }
}
