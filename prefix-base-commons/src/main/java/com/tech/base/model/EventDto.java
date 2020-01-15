package com.tech.base.model;


import com.tech.base.utils.date.DateUtil;

import java.util.UUID;

import lombok.Data;

@Data
public class EventDto<T> {
    private String time;// yyyy-MM-dd HH:mm:ss
    private String event; // 必填 事件
    private String ip;// 非必填
    private T properties;
    private String uuid;// 必填，默认uuid
    private String key;// 非必填 发送kafka 根据这个key，做有序发送，key相同发送到同一个队列里
    private String topic;
    
    public EventDto() {
        this(UUID.randomUUID().toString(), DateUtil.currentDateStr(), null);
    }

    public EventDto(T t) {
        this(UUID.randomUUID().toString(), DateUtil.currentDateStr(), t);
    }

    public EventDto(String uuit, String time, T t) {
        this.uuid = uuit;
        this.time = time;
        this.properties = t;
    }
}
