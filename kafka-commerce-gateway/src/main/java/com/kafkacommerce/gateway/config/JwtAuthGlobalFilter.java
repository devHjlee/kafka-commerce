//package com.kafkacommerce.gateway.config;
//
//import com.kafkacommerce.common.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().toString();
//
//        // 인증이 필요 없는 경로는 통과
//        if (path.startsWith("/api/auth/")) {
//            return chain.filter(exchange);
//        }
//
//        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        // String token = authHeader.substring(7);
//        String token = resolveToken(authHeader);
//        if (!jwtTokenProvider.validateToken(token)) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        // 필요하다면 claims에서 userId, role 추출 후 헤더로 downstream 서비스에 전달
//        String userId = jwtTokenProvider.getUserIdFromToken(token);
//        String role = jwtTokenProvider.getRoleFromToken(token);
//
//        exchange = exchange.mutate()
//                .request(builder -> builder
//                        .header("X-User-Id", userId)
//                        .header("X-User-Role", role)
//                ).build();
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -1; // 필터 우선순위
//    }
//
//    private String resolveToken(String authHeader) {
//        String bearerToken = authHeader;
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
