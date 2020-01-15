package com.tech.base.utils.kafka;

import com.tech.base.model.EventDto;

import java.util.ArrayList;
import java.util.List;

public class EventDtoBatchBuild<T> extends EventDto<T> {

    private List<EventDtoBuild<T>> eventMsgList;

    public EventDtoBatchBuild() {
        eventMsgList = new ArrayList<>();
    }

    public void addEventDto(EventDtoBuild<T> eventMsg) {
        eventMsgList.add(eventMsg);
    }
    
    public void addEventDtoAll(List<EventDtoBuild<T>> eventMsgList) {
        eventMsgList.addAll(eventMsgList);
    }
    
    public List<EventDtoBuild<T>> getEventMsgList() {
        return eventMsgList;
    }

    /**
     * 说明:发送
     * 
     * @return 发送失败的List&lt;EventDtoBuild&gt;
     * @date: 2019年10月28日
     */
    public List<EventDtoBuild<T>> send() {
        return KafkaUtil.send(this);
    }

}
