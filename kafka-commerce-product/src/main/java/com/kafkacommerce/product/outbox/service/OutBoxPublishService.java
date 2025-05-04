package com.kafkacommerce.product.outbox.service;

import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import com.kafkacommerce.product.outbox.enity.EventOutbox;
import com.kafkacommerce.product.outbox.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutBoxPublishService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventOutboxRepository eventOutboxRepository;

    @Transactional
    public void publish(OrderCreatedEvent event) {
        // 저장된 Outbox를 기준으로 Kafka 발행
        Optional<EventOutbox> eventOutbox = eventOutboxRepository.findFirstByAggregateIdAndTypeAndSentFalse(
                event.getOrderId(), "OrderChanged");

        eventOutbox.ifPresent(outbox -> {
            kafkaTemplate.send("stock.deducted", outbox.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            outbox.completeSent();
                            eventOutboxRepository.save(outbox);
                        } else {
                            // 실패 로깅
                        }
                    });
        });
    }
}
