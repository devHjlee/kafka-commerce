package com.kafkacommerce.user.service;

import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.dto.request.LoginRequest;
import com.kafkacommerce.user.dto.response.TokenResponse;
import com.kafkacommerce.user.repository.UserRepository;
import com.kafkacommerce.user.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider tokenProvider;
    @Mock private RefreshTokenService refreshTokenService;
    @InjectMocks private AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        LoginRequest request = new LoginRequest("test@test.com", "Password1!");
        Authentication authentication = mock(Authentication.class);
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(tokenProvider.generateAccessToken(authentication)).willReturn("accessToken");
        given(tokenProvider.generateRefreshToken("test@test.com")).willReturn("refreshToken");
        User user = User.builder().email("test@test.com").build();
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));

        TokenResponse response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void login_fail_badCredentials() {
        LoginRequest request = new LoginRequest("test@test.com", "wrongPw");
        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("Bad credentials"));
        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("리프레시 토큰 위조/만료/미존재 시 예외")
    void refreshToken_fail() {
        given(tokenProvider.validateToken("invalid")).willReturn(false);
        assertThatThrownBy(() -> authService.refreshAccessToken("invalid"))
            .isInstanceOf(RuntimeException.class); // BusinessException 등 실제 예외로 교체
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰 삭제")
    void logout_success() {
        User user = User.builder().email("test@test.com").build();
        given(tokenProvider.getEmailFromToken("refreshToken")).willReturn("test@test.com");
        given(userRepository.findByEmail("test@test.com")).willReturn(Optional.of(user));
        doNothing().when(refreshTokenService).deleteRefreshToken(1L);

        authService.logout("refreshToken");
        verify(refreshTokenService, times(1)).deleteRefreshToken(1L);
    }
} 