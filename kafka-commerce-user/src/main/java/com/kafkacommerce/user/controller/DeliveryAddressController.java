package com.kafkacommerce.user.controller;

import com.kafkacommerce.common.response.ApiResponse;
import com.kafkacommerce.commonweb.security.UserPrincipal;
import com.kafkacommerce.user.domain.DeliveryAddress;
import com.kafkacommerce.user.service.DeliveryAddressService;

import com.kafkacommerce.user.dto.request.CreateDeliveryAddressRequest;
import com.kafkacommerce.user.dto.request.UpdateDeliveryAddressRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-addresses")
@RequiredArgsConstructor
public class DeliveryAddressController {
    private final DeliveryAddressService deliveryAddressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryAddress>>> getAddresses(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<DeliveryAddress> addresses = deliveryAddressService.getAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success(addresses, "배송지 목록 조회 성공"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryAddress>> addAddress(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                  @RequestBody @Valid CreateDeliveryAddressRequest request) {
        DeliveryAddress saved = deliveryAddressService.addAddress(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(saved, "배송지 등록 성공"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryAddress>> updateAddress(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                     @PathVariable Long id,
                                                                     @RequestBody @Valid UpdateDeliveryAddressRequest request) {
        DeliveryAddress updated = deliveryAddressService.updateAddress(userPrincipal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "배송지 수정 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @PathVariable Long id) {
        deliveryAddressService.deleteAddress(userPrincipal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("배송지 삭제 성공"));
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<DeliveryAddress>> setDefaultAddress(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                         @PathVariable Long id) {
        DeliveryAddress updated = deliveryAddressService.setDefaultAddress(userPrincipal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(updated, "기본 배송지 설정 성공"));
    }
} 