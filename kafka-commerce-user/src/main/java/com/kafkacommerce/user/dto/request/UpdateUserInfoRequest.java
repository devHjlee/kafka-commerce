package com.kafkacommerce.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserInfoRequest {
    private String nickname;
    private String phoneNumber;
} 