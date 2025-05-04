package com.kafkacommerce.common.enums;

public enum OrderStatus {
    PENDING,           // 주문 접수됨
    STOCK_DEDUCTED,    // 재고 차감 완료
    STOCK_FAILED,      // 재고 부족
    PAYMENT_COMPLETED, // 결제 완료
    PAYMENT_FAILED,    // 결제 실패
    CANCELLED          // 주문 취소
}