package com.kakaopay.rainMoney.exception;

import lombok.Getter;

// TODO: 커스텀 오류 관련 구체화 필요, 에러 핸들링, HTTP 상태코드
@Getter
public class APIException extends RuntimeException {
    /**
     * API 관련 오류 정의
     */
    private final String errCode;

    public APIException(String message, String errCode) {
        super(message, null, false, false);
        this.errCode = errCode;
    }
}