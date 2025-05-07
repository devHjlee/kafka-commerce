package com.kafkacommerce.order.repository;

import com.kafkacommerce.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
