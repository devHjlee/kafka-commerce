package com.kafkacommerce.user.service;

import com.kafkacommerce.common.exception.BusinessException;
import com.kafkacommerce.common.exception.ErrorCode;
import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.dto.request.LoginRequest;
import com.kafkacommerce.user.dto.request.SignUpRequest;
import com.kafkacommerce.user.dto.response.LoginResponse;
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

    @Transactional
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "이미 존재하는 이메일입니다.");
        }

        User user = User.createUser(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName()
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        String accessToken = tokenProvider.generateToken(authentication);
        return new LoginResponse(accessToken);
    }
} 