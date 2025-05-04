package com.kafkacommerce.product.repository;

import com.kafkacommerce.product.dto.response.ProductResponse;
import com.kafkacommerce.product.dto.response.QProductResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.kafkacommerce.product.domain.QProduct;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductResponse> searchProducts(String name, Long categoryId, String status, Pageable pageable) {
        QProduct product = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();
        if (name != null && !name.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(name));
        }
        if (categoryId != null) {
            builder.and(product.categoryId.eq(categoryId));
        }
        if (status != null && !status.isEmpty()) {
            builder.and(product.status.stringValue().eq(status));
        }
        JPAQuery<ProductResponse> query = queryFactory
                .select(new QProductResponse(
                        product.id,
                        product.name,
                        product.price,
                        product.description,
                        product.categoryId,
                        product.status.stringValue(),
                        product.thumbnailUrl,
                        product.createdAt
                ))
                .from(product)
                .where(builder);
        long total = query.fetchCount();
        List<ProductResponse> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(content, pageable, total);
    }
} 