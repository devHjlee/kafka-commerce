package com.kafkacommerce.user.service;

import com.kafkacommerce.common.exception.BusinessException;
import com.kafkacommerce.common.exception.ErrorCode;
import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.dto.request.LoginRequest;
import com.kafkacommerce.user.dto.request.SignUpRequest;
import com.kafkacommerce.user.dto.response.LoginResponse;
import com.kafkacommerce.user.dto.response.TokenResponse;
import com.kafkacommerce.user.repository.UserRepository;
import com.kafkacommerce.user.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.createUser(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName()
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication.getName());

        Long userId = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
            .getId();
            
        refreshTokenService.saveRefreshToken(userId, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        // 1. 리프레시 토큰 유효성 검사
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 2. 리프레시 토큰에서 이메일 추출
        String userEmail = tokenProvider.getEmailFromToken(refreshToken);

        // 3. 레디스에서 리프레시 토큰 조회 및 검증
        Long userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
                .getId();

        String savedRefreshToken = refreshTokenService.getRefreshToken(userId);
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }

        // 4. 새로운 액세스 토큰과 리프레시 토큰 발급
        String newAccessToken = tokenProvider.generateAccessToken(
            new UsernamePasswordAuthenticationToken(userEmail, null)
        );
        String newRefreshToken = tokenProvider.generateRefreshToken(userEmail);

        // 5. 새로운 리프레시 토큰 저장
        refreshTokenService.saveRefreshToken(userId, newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        // 1. 리프레시 토큰에서 이메일 추출
        String userEmail = tokenProvider.getEmailFromToken(refreshToken);

        // 2. 레디스에서 리프레시 토큰 삭제
        Long userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
                .getId();

        refreshTokenService.deleteRefreshToken(userId);
    }
} 