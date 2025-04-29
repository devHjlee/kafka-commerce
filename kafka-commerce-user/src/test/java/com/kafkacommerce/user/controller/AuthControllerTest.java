package com.kafkacommerce.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkacommerce.user.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유효하지 않은 비밀번호로 회원가입 시도 시 GlobalExceptionHandler가 동작해야 한다")
    void signUp_withInvalidPassword_shouldReturnBadRequest() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest("test@test.com", "invalid", "테스트");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        MvcResult result = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("비밀번호는 영문 대소문자, 숫자, 특수문자(!@#$%^&*)를 포함한 8자리 이상이어야 합니다.");
    }
} 