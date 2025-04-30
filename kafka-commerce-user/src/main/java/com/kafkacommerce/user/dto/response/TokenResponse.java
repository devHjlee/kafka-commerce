package com.kafkacommerce.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
} 