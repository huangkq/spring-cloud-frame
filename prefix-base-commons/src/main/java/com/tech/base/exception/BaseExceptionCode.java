package com.tech.base.exception;

public enum BaseExceptionCode {
    ERROR_CODE_100(100, "交易失败")

    ;
    private int code;
    private String msg;

    BaseExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
