package com.kafkacommerce.user.service;

import com.kafkacommerce.common.exception.BusinessException;
import com.kafkacommerce.common.exception.ErrorCode;
import com.kafkacommerce.common.jwt.JwtTokenProvider;
import com.kafkacommerce.common.security.UserPrincipal;
import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.dto.request.LoginRequest;
import com.kafkacommerce.user.dto.request.SignUpRequest;
import com.kafkacommerce.user.dto.response.TokenResponse;
import com.kafkacommerce.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
            request.getName(),
            request.getNickname(),
            request.getPhoneNumber()
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        try {
            log.info("Attempting login for user: {}", request.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            log.info("User authenticated successfully: {}", principal.getEmail());

            String accessToken = tokenProvider.createAccessToken(
                principal.getEmail(),
                principal.getId().toString(),
                principal.getAuthorities().iterator().next().getAuthority()
            );
            String refreshToken = tokenProvider.createRefreshToken(principal.getEmail());

            refreshTokenService.saveRefreshToken(principal.getId(), refreshToken);

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", request.getEmail(), e);
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail(), e);
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", request.getEmail(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public TokenResponse refreshAccessToken(String refreshToken) {
        // 1. 리프레시 토큰 유효성 검사
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 2. 리프레시 토큰에서 이메일 추출
        String userEmail = tokenProvider.getEmailFromToken(refreshToken);

        // 3. 레디스에서 리프레시 토큰 조회 및 검증
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String savedRefreshToken = refreshTokenService.getRefreshToken(user.getId());
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }

        // 4. 새로운 액세스 토큰과 리프레시 토큰 발급
        String newAccessToken = tokenProvider.createAccessToken(
            user.getEmail(),
            user.getId().toString(),
            user.getRole().name()
        );
        String newRefreshToken = tokenProvider.createRefreshToken(user.getEmail());

        // 5. 새로운 리프레시 토큰 저장
        refreshTokenService.saveRefreshToken(user.getId(), newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public void logout(String refreshToken) {
        // 1. 리프레시 토큰에서 이메일 추출
        String userEmail = tokenProvider.getEmailFromToken(refreshToken);

        // 2. 레디스에서 리프레시 토큰 삭제
        Long userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
                .getId();

        refreshTokenService.deleteRefreshToken(userId);
    }

    @Transactional(readOnly = true)
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
} 