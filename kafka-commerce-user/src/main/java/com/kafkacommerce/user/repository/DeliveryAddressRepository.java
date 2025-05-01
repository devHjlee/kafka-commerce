package com.kafkacommerce.user.repository;

import com.kafkacommerce.user.domain.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findByUserId(Long userId);
    List<DeliveryAddress> findByUserIdOrderByIdDesc(Long userId);
    DeliveryAddress findByUserIdAndIsDefaultTrue(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
} 