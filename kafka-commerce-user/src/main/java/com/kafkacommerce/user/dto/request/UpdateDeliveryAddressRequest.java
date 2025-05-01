package com.kafkacommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeliveryAddressRequest {
    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverPhone;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String address1;
    private String address2;
    private Boolean isDefault;
} 