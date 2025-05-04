package com.kafkacommerce.product.service;

import com.kafkacommerce.common.enums.OrderStatus;
import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import com.kafkacommerce.product.domain.Stock;
import com.kafkacommerce.product.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StockService {

    private final RedissonClient redissonClient;
    private final StockRepository stockRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final String LOCK_PREFIX = "stock:lock:";

    @Transactional
    public void decreaseStock(OrderCreatedEvent event) throws InterruptedException {
        String lockKey = LOCK_PREFIX + event.getProductId();
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = lock.tryLock(3, 1, TimeUnit.SECONDS); // wait 3초, lease 1초
        if (!isLocked) {
            throw new IllegalStateException("재고 락 획득 실패");
        }

        try {
            Stock stock = stockRepository.findStockByProductIdAndOptionId(event.getProductId(), event.getOptionId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품 재고 없음"));

            stock.decrease(event.getQuantity());
            event.setStatus(OrderStatus.STOCK_DEDUCTED);
            eventPublisher.publishEvent(event);
        } catch (IllegalArgumentException e) {
            event.setStatus(OrderStatus.STOCK_FAILED);
            eventPublisher.publishEvent(event);
        } finally {
            lock.unlock();
        }
    }

    public void increaseStock(Long productId, Long optionId, int amount) throws InterruptedException {
        String lockKey = LOCK_PREFIX + productId;
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = lock.tryLock(3, 1, TimeUnit.SECONDS);
        if (!isLocked) {
            throw new IllegalStateException("재고 락 획득 실패");
        }

        try {
            Stock stock = stockRepository.findStockByProductIdAndOptionId(productId, optionId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품 재고 없음"));

            stock.increase(amount);

        } finally {
            lock.unlock();
        }
    }
}

