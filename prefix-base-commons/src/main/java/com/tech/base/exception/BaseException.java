package com.tech.base.exception;

/**
 * 说明:异常类父类,用于抛出异常动态转型, 此abstract异常类不可构造使用
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = -2423279393324395996L;
    protected int code;

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BaseException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String toString() {
        return "code:" + code + ",message:" + getLocalizedMessage();
    }
}
