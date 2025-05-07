package com.kafkacommerce.order.outbox.scheduler;

import com.kafkacommerce.order.outbox.enity.EventOutbox;
import com.kafkacommerce.order.outbox.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final EventOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> orderKafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingOutboxEvents() {
        List<EventOutbox> unsentEvents = outboxRepository.findAllBySentFalseAndType("OrderCreated");

        for (EventOutbox outbox : unsentEvents) {
            try {
                orderKafkaTemplate.send("order.created", outbox.getPayload());
                outbox.completeSent(); // sent = true 변경
                log.info("스케줄러 재전송 성공: id={}, aggregateId={}", outbox.getId(), outbox.getAggregateId());
            } catch (Exception e) {
                log.error("Kafka 전송 실패 (재시도 예정): id={}, error={}", outbox.getId(), e.getMessage());
            }
        }
    }
}

