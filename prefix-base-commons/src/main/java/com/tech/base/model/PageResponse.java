package com.tech.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 说明:分页返回数据结构
 * 
 * @author huangkeqi
 * @date 2020年5月21日
 */
@Getter
@Setter
public class PageResponse<T> extends Response<T> {

    private String cursor;// 光标

    public PageResponse(T t, String cursor) {
        super();
        this.setData(t);
        this.cursor = cursor;
    }
}
