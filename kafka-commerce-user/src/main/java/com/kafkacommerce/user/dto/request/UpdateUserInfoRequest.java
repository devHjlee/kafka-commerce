package com.kafkacommerce.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {
    private String nickname;
    private String phoneNumber;
} 