package com.kafkacommerce.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkacommerce.user.dto.request.ChangePasswordRequest;
import com.kafkacommerce.user.dto.request.LoginRequest;
import com.kafkacommerce.user.dto.request.SignUpRequest;
import com.kafkacommerce.user.dto.request.UpdateUserInfoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;

    @BeforeEach
    void setUp() throws Exception {
        // 회원가입 및 로그인 후 accessToken 획득
        var signUp = new SignUpRequest("me@test.com", "Password1!", "테스트", "닉네임", "01012345678");
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isOk());

        var login = new LoginRequest("me@test.com", "Password1!");
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        this.accessToken = objectMapper.readTree(responseBody).get("data").get("accessToken").asText();
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void getMyInfo_success() throws Exception {
        var result = mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String email = objectMapper.readTree(responseBody).get("data").get("email").asText();

        assertThat(email).isEqualTo("me@test.com");
    }

    @Test
    @DisplayName("내 정보 수정 성공")
    void updateMyInfo_success() throws Exception {
        var updateRequest = new UpdateUserInfoRequest("새닉네임", "01099998888");
        mockMvc.perform(put("/api/users/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("새닉네임"));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword_success() throws Exception {
        var pwRequest = new ChangePasswordRequest("Password1!", "NewPassword1!");
        mockMvc.perform(put("/api/users/me/password")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pwRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호 변경 성공"));
    }
}