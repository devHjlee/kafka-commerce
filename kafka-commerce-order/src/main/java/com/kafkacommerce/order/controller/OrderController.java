package com.kafkacommerce.order.controller;

import com.kafkacommerce.common.response.ApiResponse;
import com.kafkacommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createOrder(@RequestParam Long productId,
                                                        @RequestParam Long optionId,
                                                        @RequestParam int quantity) {
        Long orderId = orderService.createOrder(productId, optionId, quantity);
        return ResponseEntity.ok(ApiResponse.success(orderId,"주문 성공"));
    }
}