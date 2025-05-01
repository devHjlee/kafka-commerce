package com.kafkacommerce.user.service;

import com.kafkacommerce.user.domain.DeliveryAddress;
import com.kafkacommerce.user.repository.DeliveryAddressRepository;
import com.kafkacommerce.user.dto.request.CreateDeliveryAddressRequest;
import com.kafkacommerce.user.dto.request.UpdateDeliveryAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {
    private final DeliveryAddressRepository deliveryAddressRepository;

    @Transactional(readOnly = true)
    public List<DeliveryAddress> getAddresses(Long userId) {
        return deliveryAddressRepository.findByUserIdOrderByIdDesc(userId);
    }

    @Transactional
    public DeliveryAddress addAddress(Long userId, CreateDeliveryAddressRequest request) {
        // 기본 배송지로 설정 시 기존 기본 배송지 해제
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            DeliveryAddress currentDefault = deliveryAddressRepository.findByUserIdAndIsDefaultTrue(userId);
            if (currentDefault != null) {
                currentDefault.unsetDefault();
                deliveryAddressRepository.save(currentDefault);
            }
        }
        DeliveryAddress address = DeliveryAddress.builder()
                .userId(userId)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .zipcode(request.getZipcode())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .isDefault(request.getIsDefault())
                .build();
        return deliveryAddressRepository.save(address);
    }

    @Transactional
    public DeliveryAddress updateAddress(Long userId, Long addressId, UpdateDeliveryAddressRequest request) {
        DeliveryAddress address = deliveryAddressRepository.findById(addressId)
                .filter(a -> a.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));
        address.updateAddress(request.getReceiverName(), request.getReceiverPhone(), request.getZipcode(), request.getAddress1(), request.getAddress2());
        // 기본 배송지 변경 처리
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            DeliveryAddress currentDefault = deliveryAddressRepository.findByUserIdAndIsDefaultTrue(userId);
            if (currentDefault != null && !currentDefault.getId().equals(addressId)) {
                currentDefault.unsetDefault();
                deliveryAddressRepository.save(currentDefault);
            }
            address.setAsDefault();
        } else {
            address.unsetDefault();
        }
        return deliveryAddressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        DeliveryAddress address = deliveryAddressRepository.findById(addressId)
                .filter(a -> a.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));
        boolean wasDefault = Boolean.TRUE.equals(address.getIsDefault());
        deliveryAddressRepository.delete(address);
        // 기본 배송지 삭제 시 남은 주소 중 하나를 기본으로
        if (wasDefault) {
            List<DeliveryAddress> addresses = deliveryAddressRepository.findByUserIdOrderByIdDesc(userId);
            if (!addresses.isEmpty()) {
                DeliveryAddress first = addresses.get(0);
                first.setAsDefault();
                deliveryAddressRepository.save(first);
            }
        }
    }

    @Transactional
    public DeliveryAddress setDefaultAddress(Long userId, Long addressId) {
        List<DeliveryAddress> addresses = deliveryAddressRepository.findByUserId(userId);
        for (DeliveryAddress addr : addresses) {
            if (addr.getId().equals(addressId)) {
                addr.setAsDefault();
            } else {
                addr.unsetDefault();
            }
            deliveryAddressRepository.save(addr);
        }
        return deliveryAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));
    }
} 