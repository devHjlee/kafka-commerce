package com.kafkacommerce.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//    private final JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // 모든 요청을 GlobalFilter에서 처리
                );
        return http.build();
    }
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authenticationManager(jwtReactiveAuthenticationManager)
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/api/auth/**").permitAll()
//                        .anyExchange().authenticated()
//                );
//        return http.build();
//    }
}

