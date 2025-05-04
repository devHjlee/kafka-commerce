package com.kafkacommerce.product.outbox.repository;

import com.kafkacommerce.product.outbox.enity.EventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventOutboxRepository extends JpaRepository<EventOutbox, Long> {

    Optional<EventOutbox> findFirstByAggregateIdAndTypeAndSentFalse(Long aggregateId, String type);
    List<EventOutbox> findAllBySentFalseAndType(String type);
}

