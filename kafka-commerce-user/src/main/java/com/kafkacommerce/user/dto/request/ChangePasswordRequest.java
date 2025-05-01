package com.kafkacommerce.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
} 