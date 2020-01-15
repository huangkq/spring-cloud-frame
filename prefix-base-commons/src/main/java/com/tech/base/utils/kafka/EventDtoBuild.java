package com.tech.base.utils.kafka;

import com.tech.base.model.EventDto;

import org.apache.commons.lang3.StringUtils;

public class EventDtoBuild<T> extends EventDto<T> {

    public EventDtoBuild() {}

    public EventDtoBuild(T t) {
        super(t);
    }

    public EventDtoBuild(String uuit, String time, T t) {
        super(uuit, time, t);
    }

    public EventDtoBuild<T> withUuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public EventDtoBuild<T> withEvent(String event) {
        this.setEvent(event);
        return this;
    }

    public EventDtoBuild<T> withIp(String ip) {
        this.setIp(ip);
        return this;
    }

    public EventDtoBuild<T> withProperties(T data) {
        this.setProperties(data);
        return this;
    }

    public EventDtoBuild<T> withTopic(String topic) {
        this.setTopic(topic);
        return this;
    }

    /** 发送kafka 根据这个key，做有序发送，key相同发送到同一个队列里 */
    public EventDtoBuild<T> withOrderKey(String key) {
        this.setKey(key);
        return this;
    }

    public void check() {
        if (StringUtils.isBlank(this.getEvent())) throw new RuntimeException("event required");
        if (StringUtils.isBlank(this.getUuid())) throw new RuntimeException("uuid required");
        if (StringUtils.isBlank(this.getTopic())) throw new RuntimeException("topic required");
    }

    /** 发送 */
    public void send() {
        this.check();
        KafkaUtil.send(this, this.getTopic());
    }

    /** 发送 */
    public void send(String topic) {
        this.withTopic(topic);
        this.send();
    }

}
