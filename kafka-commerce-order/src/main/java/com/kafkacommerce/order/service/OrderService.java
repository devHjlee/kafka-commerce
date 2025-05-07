package com.kafkacommerce.order.service;

import com.kafkacommerce.common.enums.OrderStatus;
import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import com.kafkacommerce.order.domain.Order;
import com.kafkacommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Long createOrder(Long productId, Long optionId, int quantity) {
        Order order = Order.builder()
                .productId(productId)
                .quantity(quantity)
                .status(OrderStatus.PENDING)
                .optionId(optionId)
                .build();

        Order orderSave = orderRepository.save(order);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(orderSave.getId())
                .productId(orderSave.getProductId())
                .optionId(orderSave.getOptionId())
                .quantity(orderSave.getQuantity())
                .orderStatus(orderSave.getStatus())
                .build();

        // Spring 이벤트 발행 : BEFORE_COMMIT / AFTER_COMMIT 리스너에서 각각 처리
        publisher.publishEvent(event);

        return orderSave.getId();
    }
}
