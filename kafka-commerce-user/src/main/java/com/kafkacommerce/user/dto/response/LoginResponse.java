package com.kafkacommerce.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String email;
    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";
} 