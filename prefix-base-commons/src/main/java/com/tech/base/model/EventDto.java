package com.tech.base.model;


import com.tech.base.utils.date.DateUtil;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class EventDto<T> {
    private LocalDateTime time;// yyyy-MM-dd HH:mm:ss
    private Integer eventType;// 必填 事件类型
    private T data;
    private String uniqueSerial;// 必填，唯一流水号
    private String key;// 非必填 发送kafka 根据这个key，做有序发送，key相同发送到同一个队列里
    private String topic;

    public EventDto() {
        this(UUID.randomUUID().toString(), DateUtil.currentLocalDateTime(), null);
    }

    public EventDto(T t) {
        this(UUID.randomUUID().toString(), DateUtil.currentLocalDateTime(), t);
    }

    public EventDto(String uniqueSerial, LocalDateTime time, T t) {
        this.uniqueSerial = uniqueSerial;
        this.time = time;
        this.data = t;
    }
}
