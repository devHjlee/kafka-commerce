package com.kafkacommerce.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import com.kafkacommerce.product.domain.Product;
import com.kafkacommerce.product.domain.ProductStatus;
import com.kafkacommerce.product.dto.request.ProductCreateRequest;
import com.kafkacommerce.product.dto.response.ProductResponse;
import com.kafkacommerce.product.repository.CategoryRepository;
import com.kafkacommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_success() {
        // given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Test Product");
        request.setPrice(1000L);
        request.setDescription("Description");
        request.setCategoryId(1L);
        request.setThumbnailUrl("http://example.com/thumb.jpg");

        given(categoryRepository.existsById(1L)).willReturn(true);
        LocalDateTime now = LocalDateTime.now();
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ReflectionTestUtils.setField(p, "id", 1L);
            ReflectionTestUtils.setField(p, "createdAt", now);
            return p;
        });

        // when
        ProductResponse response = productService.createProduct(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals(1000L, response.getPrice());
        assertEquals("Description", response.getDescription());
        assertEquals(1L, response.getCategoryId());
        assertEquals("AVAILABLE", response.getStatus());
        assertEquals("http://example.com/thumb.jpg", response.getThumbnailUrl());
        assertEquals(now, response.getCreatedAt());

        then(categoryRepository).should().existsById(1L);
        then(productRepository).should().save(any(Product.class));
    }

    @Test
    void createProduct_categoryNotFound_throws() {
        // given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Test");
        request.setPrice(500L);
        request.setCategoryId(2L);

        given(categoryRepository.existsById(2L)).willReturn(false);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));
        assertEquals("존재하지 않는 카테고리입니다.", exception.getMessage());
        then(categoryRepository).should().existsById(2L);
        then(productRepository).shouldHaveNoInteractions();
    }

    @Test
    void getProduct_success() {
        // given
        Long id = 1L;
        Product product = mock(Product.class);
        given(productRepository.findById(id)).willReturn(Optional.of(product));
        given(product.getId()).willReturn(1L);
        given(product.getName()).willReturn("Name");
        given(product.getPrice()).willReturn(2000L);
        given(product.getDescription()).willReturn("Desc");
        given(product.getCategoryId()).willReturn(1L);
        given(product.getStatus()).willReturn(ProductStatus.AVAILABLE);
        given(product.getThumbnailUrl()).willReturn("thumb");
        LocalDateTime createdAt = LocalDateTime.now();
        given(product.getCreatedAt()).willReturn(createdAt);

        // when
        ProductResponse response = productService.getProduct(id);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Name", response.getName());
        assertEquals(2000L, response.getPrice());
        assertEquals("Desc", response.getDescription());
        assertEquals(1L, response.getCategoryId());
        assertEquals("AVAILABLE", response.getStatus());
        assertEquals("thumb", response.getThumbnailUrl());
        assertEquals(createdAt, response.getCreatedAt());

        then(productRepository).should().findById(id);
    }

    @Test
    void getProduct_notFound_throws() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> productService.getProduct(1L));
        assertEquals("존재하지 않는 상품입니다.", exception.getMessage());
        then(productRepository).should().findById(1L);
    }

    @Test
    void getProducts_returnsPage() {
        // given
        String name = "someName";
        Long categoryId = 1L;
        String status = "AVAILABLE";
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponse resp1 = ProductResponse.builder()
                .id(1L).name("A").price(100L).description("DescA").categoryId(1L)
                .status("AVAILABLE").thumbnailUrl("thumbA").createdAt(LocalDateTime.now()).build();
        ProductResponse resp2 = ProductResponse.builder()
                .id(2L).name("B").price(200L).description("DescB").categoryId(1L)
                .status("AVAILABLE").thumbnailUrl("thumbB").createdAt(LocalDateTime.now()).build();
        Page<ProductResponse> page = new PageImpl<>(Arrays.asList(resp1, resp2), pageable, 2);
        given(productRepository.searchProducts(name, categoryId, status, pageable)).willReturn(page);

        // when
        Page<ProductResponse> result = productService.getProducts(name, categoryId, status, pageable);

        // then
        then(productRepository).should().searchProducts(name, categoryId, status, pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(Arrays.asList(resp1, resp2), result.getContent());
    }

    @Test
    void updateProduct_success() {
        // given
        Long id = 1L;
        Product product = mock(Product.class);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("NewName");
        request.setPrice(3000L);
        request.setDescription("NewDesc");
        request.setCategoryId(2L);
        request.setThumbnailUrl("newThumb");

        given(product.getId()).willReturn(id);
        given(product.getName()).willReturn("NewName");
        given(product.getPrice()).willReturn(3000L);
        given(product.getDescription()).willReturn("NewDesc");
        given(product.getCategoryId()).willReturn(2L);
        given(product.getStatus()).willReturn(ProductStatus.AVAILABLE);
        given(product.getThumbnailUrl()).willReturn("newThumb");
        LocalDateTime createdAt2 = LocalDateTime.now();
        given(product.getCreatedAt()).willReturn(createdAt2);

        // when
        ProductResponse response = productService.updateProduct(id, request);

        // then
        then(productRepository).should().findById(id);
        then(product).should().update("NewName", 3000L, "NewDesc", 2L, ProductStatus.AVAILABLE, "newThumb");
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("NewName", response.getName());
        assertEquals(3000L, response.getPrice());
        assertEquals("NewDesc", response.getDescription());
        assertEquals(2L, response.getCategoryId());
        assertEquals("AVAILABLE", response.getStatus());
        assertEquals("newThumb", response.getThumbnailUrl());
        assertEquals(createdAt2, response.getCreatedAt());
    }

    @Test
    void updateProduct_notFound_throws() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        ProductCreateRequest request = new ProductCreateRequest();

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(1L, request));
        assertEquals("존재하지 않는 상품입니다.", exception.getMessage());
        then(productRepository).should().findById(1L);
    }

    @Test
    void deleteProduct_success() {
        // given
        Long id = 1L;
        Product product = mock(Product.class);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        // when
        productService.deleteProduct(id);

        // then
        then(productRepository).should().findById(id);
        then(product).should().changeStatus(ProductStatus.HIDDEN);
    }

    @Test
    void deleteProduct_notFound_throws() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(1L));
        assertEquals("존재하지 않는 상품입니다.", exception.getMessage());
        then(productRepository).should().findById(1L);
    }
} 