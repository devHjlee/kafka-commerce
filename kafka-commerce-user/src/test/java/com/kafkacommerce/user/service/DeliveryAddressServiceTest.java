package com.kafkacommerce.user.service;

import com.kafkacommerce.user.domain.DeliveryAddress;
import com.kafkacommerce.user.dto.request.CreateDeliveryAddressRequest;
import com.kafkacommerce.user.dto.request.UpdateDeliveryAddressRequest;
import com.kafkacommerce.user.repository.DeliveryAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryAddressServiceTest {
    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;
    @InjectMocks
    private DeliveryAddressService deliveryAddressService;

    private DeliveryAddress address;

    @BeforeEach
    void setUp() {
        address = DeliveryAddress.builder()
                .userId(1L)
                .receiverName("홍길동")
                .receiverPhone("01012345678")
                .zipcode("12345")
                .address1("서울시 강남구")
                .address2("101동 202호")
                .isDefault(true)
                .build();
    }

    @Test
    @DisplayName("배송지 등록 성공")
    void addAddress_success() {
        CreateDeliveryAddressRequest request = new CreateDeliveryAddressRequest();
        request.setReceiverName("홍길동");
        request.setReceiverPhone("01012345678");
        request.setZipcode("12345");
        request.setAddress1("서울시 강남구");
        request.setAddress2("101동 202호");
        request.setIsDefault(true);
        given(deliveryAddressRepository.save(any(DeliveryAddress.class))).willReturn(address);
        DeliveryAddress result = deliveryAddressService.addAddress(1L, request);
        assertThat(result.getReceiverName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("배송지 목록 조회 성공")
    void getAddresses_success() {
        List<DeliveryAddress> list = new ArrayList<>();
        list.add(address);
        given(deliveryAddressRepository.findByUserIdOrderByIdDesc(1L)).willReturn(list);
        List<DeliveryAddress> result = deliveryAddressService.getAddresses(1L);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("배송지 수정 성공")
    void updateAddress_success() {
        UpdateDeliveryAddressRequest request = new UpdateDeliveryAddressRequest();
        request.setReceiverName("김철수");
        request.setReceiverPhone("01099998888");
        request.setZipcode("54321");
        request.setAddress1("부산시 해운대구");
        request.setAddress2("");
        request.setIsDefault(false);
        given(deliveryAddressRepository.findById(1L)).willReturn(Optional.of(address));
        given(deliveryAddressRepository.save(any(DeliveryAddress.class))).willReturn(address);
        DeliveryAddress result = deliveryAddressService.updateAddress(1L, 1L, request);
        assertThat(result.getReceiverName()).isEqualTo("김철수");
    }

    @Test
    @DisplayName("배송지 삭제 성공")
    void deleteAddress_success() {
        given(deliveryAddressRepository.findById(1L)).willReturn(Optional.of(address));
        doNothing().when(deliveryAddressRepository).delete(address);
        deliveryAddressService.deleteAddress(1L, 1L);
        verify(deliveryAddressRepository, times(1)).delete(address);
    }

    @Test
    @DisplayName("기본 배송지 설정 성공")
    void setDefaultAddress_success() {
        DeliveryAddress another = DeliveryAddress.builder()
                .userId(1L)
                .receiverName("김철수")
                .receiverPhone("01099998888")
                .zipcode("54321")
                .address1("부산시 해운대구")
                .address2("")
                .isDefault(false)
                .build();
        List<DeliveryAddress> list = new ArrayList<>();
        list.add(address);
        list.add(another);
        given(deliveryAddressRepository.findByUserId(1L)).willReturn(list);
        given(deliveryAddressRepository.findById(1L)).willReturn(Optional.of(address));
        given(deliveryAddressRepository.save(any(DeliveryAddress.class))).willReturn(address);
        DeliveryAddress result = deliveryAddressService.setDefaultAddress(1L, 1L);
        assertThat(result.getIsDefault()).isTrue();
    }
} 