package com.kafkacommerce.user.domain;

import jakarta.persistence.*;
import lombok.*;
import com.kafkacommerce.common.entity.BaseEntity;

@Entity
@Table(name = "delivery_address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String receiverName;

    @Column(nullable = false, length = 20)
    private String receiverPhone;

    @Column(nullable = false, length = 10)
    private String zipcode;

    @Column(nullable = false, length = 200)
    private String address1;

    @Column(length = 200)
    private String address2;

    @Column(nullable = false)
    private Boolean isDefault;

    @Builder
    private DeliveryAddress(Long userId, String receiverName, String receiverPhone, String zipcode, String address1, String address2, Boolean isDefault) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
        this.isDefault = isDefault;
    }

    // 비즈니스 메서드
    public void updateAddress(String receiverName, String receiverPhone, String zipcode, String address1, String address2) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetDefault() {
        this.isDefault = false;
    }
} 