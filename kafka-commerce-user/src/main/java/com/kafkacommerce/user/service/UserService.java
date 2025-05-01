package com.kafkacommerce.user.service;

import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.domain.UserStatus;
import com.kafkacommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kafkacommerce.common.exception.BusinessException;
import com.kafkacommerce.common.exception.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.changeStatus(UserStatus.DELETED);
    }

    @Transactional(readOnly = true)
    public User getUserInfoByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public User updateUserInfoByEmail(String email, String nickname, String phoneNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.updateProfile(nickname, phoneNumber);
        return user;
    }

    @Transactional
    public void changePasswordByEmail(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        user.changePassword(passwordEncoder.encode(newPassword));
    }
} 