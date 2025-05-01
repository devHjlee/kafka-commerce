package com.kafkacommerce.user.controller;

import com.kafkacommerce.common.response.ApiResponse;
import com.kafkacommerce.user.dto.request.LoginRequest;
import com.kafkacommerce.user.dto.request.SignUpRequest;
import com.kafkacommerce.user.dto.response.LoginResponse;
import com.kafkacommerce.user.dto.response.TokenResponse;
import com.kafkacommerce.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(request);

        // 리프레시 토큰을 httpOnly 쿠키로 설정
        Cookie refreshCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // HTTPS에서만 전송
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 14); // 14일
        response.addCookie(refreshCookie);

        LoginResponse loginResponse = LoginResponse.builder()
                .email(request.getEmail())
                .accessToken(tokenResponse.getAccessToken())
                .build();

        return ResponseEntity.ok(ApiResponse.success(loginResponse, "로그인이 완료되었습니다."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {
        TokenResponse tokenResponse = authService.refreshAccessToken(refreshToken);
        
        // 새로운 리프레시 토큰을 httpOnly 쿠키로 설정
        Cookie refreshCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 14);
        response.addCookie(refreshCookie);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {
        authService.logout(refreshToken);
        
        // 리프레시 토큰 쿠키 삭제
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        boolean available = authService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(ApiResponse.success(available, available ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다."));
    }
}