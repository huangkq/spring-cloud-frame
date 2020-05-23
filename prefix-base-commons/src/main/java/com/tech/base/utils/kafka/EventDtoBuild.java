package com.tech.base.utils.kafka;

import com.tech.base.model.EventDto;
import com.tech.base.utils.ValidateUtil;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public class EventDtoBuild<T> extends EventDto<T> {

    public EventDtoBuild() {}

    public EventDtoBuild(T t) {
        super(t);
    }

    public EventDtoBuild(String uniqueSerial, LocalDateTime time, T t) {
        super(uniqueSerial, time, t);
    }

    public EventDtoBuild<T> withUniqueSerial(String uniqueSerial) {
        this.setUniqueSerial(uniqueSerial);
        return this;
    }

    public EventDtoBuild<T> withData(T data) {
        this.setData(data);
        return this;
    }

    public EventDtoBuild<T> withTopic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public EventDtoBuild<T> withEventType(Integer eventType) {
        this.setEventType(eventType);
        return this;
    }

    /** 发送kafka 根据这个key，做有序发送，key相同发送到同一个队列里 */
    public EventDtoBuild<T> withOrderKey(String key) {
        this.setKey(key);
        return this;
    }

    public void check() {
        ValidateUtil.assertTrue(ValidateUtil.isNotNull(this.getEventType()), "event type required");
        ValidateUtil.assertTrue(StringUtils.isBlank(this.getUniqueSerial()), "uniqueSerial required");
        ValidateUtil.assertTrue(StringUtils.isBlank(this.getTopic()), "topic required");
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
