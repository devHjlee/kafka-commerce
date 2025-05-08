package com.kafkacommerce.user.controller;

import com.kafkacommerce.common.response.ApiResponse;

import com.kafkacommerce.commonweb.security.UserPrincipal;
import com.kafkacommerce.user.domain.User;
import com.kafkacommerce.user.dto.request.UpdateUserInfoRequest;
import com.kafkacommerce.user.dto.request.ChangePasswordRequest;
import com.kafkacommerce.user.service.AuthService;
import com.kafkacommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    // 닉네임 중복 체크
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        boolean available = authService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(ApiResponse.success(available, available ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다."));
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMyInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.getUserInfoByEmail(userPrincipal.getEmail());
        return ResponseEntity.ok(ApiResponse.success(user, "내 정보 조회 성공"));
    }

    // 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<User>> updateMyInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @RequestBody UpdateUserInfoRequest request) {
        User user = userService.updateUserInfoByEmail(userPrincipal.getEmail(), request.getNickname(), request.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.success(user, "내 정보 수정 성공"));
    }

    // 비밀번호 변경
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @RequestBody ChangePasswordRequest request) {
        userService.changePasswordByEmail(userPrincipal.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("비밀번호 변경 성공"));
    }
}