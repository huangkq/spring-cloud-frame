package com.tech.base.exception;

public class SystemException extends BaseException {

    /****/
    private static final long serialVersionUID = 728158338269943018L;
    
    public SystemException(int code, String msg) {
        super(code, msg);
    }

    public SystemException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
