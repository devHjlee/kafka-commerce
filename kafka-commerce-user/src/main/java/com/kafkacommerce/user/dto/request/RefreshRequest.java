package com.kafkacommerce.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshRequest {
    private String refreshToken;
} 