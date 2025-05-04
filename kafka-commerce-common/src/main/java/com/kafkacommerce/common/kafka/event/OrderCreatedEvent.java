package com.kafkacommerce.common.kafka.event;

import com.kafkacommerce.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long productId;
    private Long optionId;
    private int quantity;
    private Long userId;
    private OrderStatus status;
}
