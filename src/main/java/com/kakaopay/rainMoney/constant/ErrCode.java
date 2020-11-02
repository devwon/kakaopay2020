package com.kakaopay.rainMoney.constant;

public enum ErrCode {
    /**
     * 에러코드 정의
     */
    // HttpStatus 500
    E0000("000", "예상치 못한 오류가 발생하였습니다."),
    E0001("001", "TOKEN 발급에 실패하였습니다."),

    // HttpStatus 400
    E0101("101", "이미 해당 뿌리기 금액을 받으셨네요! 받기는 한번씩만 가능합니다."),
    E0102("102", "조회할 수 있는 기간이 지났습니다."),
    E0103("103", "데이터가 존재하지 않습니다."),
    E0104("104", "본인이 뿌린 것은 본인이 받을 수 없습니다."),
    E0105("105", "뿌릴 금액은 뿌릴 인원 이상이어야 합니다."),
    E0106("106", "요청값 형식이 잘못되었습니다."),
    E0107("107", "마감되었습니다. 다음 기회에!"),
    E0108("108", "뿌릴 인원은 채팅방내 인원수 이하이어야 합니다."),

    // HttpStatus 403
    E0301("301", "내역 조회는 발급자만 가능합니다."),
    E0302("302", "해당 대화방에 소속되어있지 않습니다.");


    public final String code;
    public final String errMsg;

    ErrCode(String code, String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }

    public String getCode(){
        return code;
    }

    public String getErrMsg(){
        return  errMsg;
    }
}
