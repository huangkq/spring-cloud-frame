package com.tech.base.response;

import com.tech.base.constants.ResponseConstants;

import lombok.Data;

@Data
public class RpcResponse<T> {
    private String cursor;// 光标
    private Integer code;// 0正常
    private String message;

    private T data;

    public RpcResponse() {
        this(ResponseConstants.SUCCESS, ResponseConstants.SUCCESS_MSG);
    }

    public RpcResponse(Integer code, String message) {
        this(code, message, null);
    }

    public RpcResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RpcResponse(T t) {
        this();
        this.data = t;
    }

    public RpcResponse(T t, String cursor) {
        this();
        this.data = t;
        this.cursor = cursor;
    }
}
