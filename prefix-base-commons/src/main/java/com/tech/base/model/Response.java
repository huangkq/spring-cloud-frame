package com.tech.base.model;

import com.tech.base.constants.BaseConstants;

import lombok.Data;

/**
 * 说明:返回数据结构
 * 
 * @author huangkeqi
 * @date 2020年5月21日
 */
@Data
public class Response<T> {
    private Integer code;// 0正常
    private String message;

    private T data;

    public Response() {
        this(BaseConstants.SUCCESS, BaseConstants.SUCCESS_MSG);
    }

    public Response(Integer code, String message) {
        this(code, message, null);
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(T t) {
        this(BaseConstants.SUCCESS, BaseConstants.SUCCESS_MSG, t);
    }

}
