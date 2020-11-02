package com.kakaopay.rainMoney.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 공통적으로 쓰이는 유틸성 함수
 */
public class CommonUtils {
    /**
     * 입력된 자리수만큼 토큰을 발급한다.
     * @param len
     * @return token
     */
    public String createToken(int len){
        return RandomStringUtils.randomAlphanumeric(len);
    }
}