package com.kafkacommerce.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkacommerce.product.dto.ProductCreateRequest;
import com.kafkacommerce.product.dto.ProductResponse;
import com.kafkacommerce.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("상품 등록 성공 - SELLER 권한")
    @WithMockUser(roles = "SELLER")
    void createProduct_success() throws Exception {
        ProductCreateRequest req = new ProductCreateRequest();
        req.setName("테스트 상품");
        req.setPrice(10000L);
        req.setCategoryId(1L);
        ProductResponse res = ProductResponse.builder()
                .id(1L)
                .name("테스트 상품")
                .price(10000L)
                .categoryId(1L)
                .status("AVAILABLE")
                .build();
        given(productService.createProduct(any())).willReturn(res);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("테스트 상품"));
    }

    @Test
    @DisplayName("상품 등록 실패 - SELLER 권한 없음")
    @WithMockUser(roles = "USER")
    void createProduct_forbidden() throws Exception {
        ProductCreateRequest req = new ProductCreateRequest();
        req.setName("테스트 상품");
        req.setPrice(10000L);
        req.setCategoryId(1L);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
} 