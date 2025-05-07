package com.kafkacommerce.order.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import com.kafkacommerce.order.outbox.enity.EventOutbox;
import com.kafkacommerce.order.outbox.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OutboxBeforeCommitListener {

    private final EventOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(OrderCreatedEvent event) throws JsonProcessingException {
        EventOutbox outbox = EventOutbox.builder()
                .aggregateType("Order")
                .aggregateId(event.getOrderId())
                .type("OrderCreated")
                .payload(toJson(event))
                .createdAt(LocalDateTime.now())
                .sent(false)
                .build();

        outboxRepository.save(outbox);
    }

    private String toJson(OrderCreatedEvent event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }
}
