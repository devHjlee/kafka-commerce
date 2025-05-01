package com.kafkacommerce.user.service;

import com.kafkacommerce.common.exception.BusinessException;
import com.kafkacommerce.common.exception.ErrorCode;
import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.domain.UserRole;
import com.kafkacommerce.user.domain.UserStatus;
import com.kafkacommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@test.com")
                .password("encodedPassword")
                .name("테스트")
                .role(UserRole.USER)
                .nickname("닉네임")
                .phoneNumber("01012345678")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void getUserInfoByEmail_success() {
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        User result = userService.getUserInfoByEmail("test@test.com");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("내 정보 조회 실패 - 존재하지 않는 사용자")
    void getUserInfoByEmail_fail() {
        given(userRepository.findByEmail("notfound@test.com")).willReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserInfoByEmail("notfound@test.com"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("내 정보 수정 성공")
    void updateUserInfoByEmail_success() {
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        User result = userService.updateUserInfoByEmail("test@test.com", "새닉네임", "01099998888");
        assertThat(result.getNickname()).isEqualTo("새닉네임");
        assertThat(result.getPhoneNumber()).isEqualTo("01099998888");
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePasswordByEmail_success() {
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("oldPw", "encodedPassword")).willReturn(true);
        given(passwordEncoder.encode("newPw")).willReturn("encodedNewPw");
        userService.changePasswordByEmail("test@test.com", "oldPw", "newPw");
        assertThat(user.getPassword()).isEqualTo("encodedNewPw");
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 불일치")
    void changePasswordByEmail_fail_wrongPassword() {
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongPw", "encodedPassword")).willReturn(false);
        assertThatThrownBy(() -> userService.changePasswordByEmail("test@test.com", "wrongPw", "newPw"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void withdraw_success() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        userService.withdraw(1L);
        assertThat(user.getStatus()).isEqualTo(UserStatus.DELETED);
    }
} 