package com.kafkacommerce.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 인증 관련 에러
    INVALID_PASSWORD(400, "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함한 8자리 이상이어야 합니다."),
    DUPLICATE_EMAIL(400, "이미 존재하는 이메일입니다."),
    INVALID_EMAIL(400, "유효하지 않은 이메일 형식입니다."),
    INVALID_NAME(400, "이름은 2자 이상 20자 이하여야 합니다."),
    INVALID_CREDENTIALS(401, "아이디 또는 비밀번호가 올바르지 않습니다."),

    // 토큰 관련 에러
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    TOKEN_NOT_FOUND(401, "토큰이 존재하지 않습니다."),

    // 사용자 관련 에러
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    INVALID_USER(400, "유효하지 않은 사용자입니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),

    // 공통 예외
    ACCESS_DENIED(403, "접근 권한이 없습니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP 메서드입니다."),
    MISSING_PARAMETER(400, "필수 파라미터가 누락되었습니다."),
    TYPE_MISMATCH(400, "파라미터 타입이 올바르지 않습니다."),
    ENTITY_NOT_FOUND(404, "엔티티를 찾을 수 없습니다."),

    // Auth
    AUTHENTICATION_FAILED(401, "Authentication failed");

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
