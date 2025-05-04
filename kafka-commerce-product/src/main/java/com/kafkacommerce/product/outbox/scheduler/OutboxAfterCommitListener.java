package com.kafkacommerce.product.outbox.scheduler;

import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import com.kafkacommerce.product.outbox.service.OutBoxPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class OutboxAfterCommitListener {

    private final OutBoxPublishService outBoxPublishService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishToKafka(OrderCreatedEvent event) {
        outBoxPublishService.publish(event);
    }
}

