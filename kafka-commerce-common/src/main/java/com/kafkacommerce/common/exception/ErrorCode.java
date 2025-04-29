package com.kafkacommerce.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러
    INVALID_INPUT_VALUE("C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED("C002", "지원하지 않는 HTTP 메소드입니다."),
    INTERNAL_SERVER_ERROR("C003", "서버 내부 오류가 발생했습니다."),
    
    // 인증/권한 에러
    UNAUTHORIZED("A001", "인증이 필요합니다."),
    FORBIDDEN("A002", "접근 권한이 없습니다."),
    
    // 비즈니스 에러
    ENTITY_NOT_FOUND("B001", "엔티티를 찾을 수 없습니다."),
    DUPLICATE_ENTITY("B002", "이미 존재하는 엔티티입니다."),
    INVALID_STATE("B003", "잘못된 상태입니다.");

    private final String code;
    private final String message;
} 