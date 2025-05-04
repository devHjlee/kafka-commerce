package com.kafkacommerce.product.kafka;

import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import com.kafkacommerce.product.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventConsumer {

    private final StockService stockService;

    @KafkaListener(topics = "order.created", groupId = "stock-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("[Kafka] 주문 생성 이벤트 수신: {}", event);

        try {
            stockService.decreaseStock(event.getProductId(), event.getOptionId(), event.getQuantity());
            log.info("[Kafka] 재고 차감 성공 - 상품ID: {}, 수량: {}", event.getProductId(), event.getQuantity());
        } catch (Exception e) {
            log.error("[Kafka] 재고 차감 실패: {}", e.getMessage(), e);

        }

    }
}
