package com.kafkacommerce.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refresh:";
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, refreshExpiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
    }
} 