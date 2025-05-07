package com.kafkacommerce.order.outbox.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_outbox")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;
    private Long aggregateId;

    private String type;

    @Lob
    private String payload;

    private LocalDateTime createdAt;

    private boolean sent;

    public void completeSent() {
        this.sent = true;
    }
}
