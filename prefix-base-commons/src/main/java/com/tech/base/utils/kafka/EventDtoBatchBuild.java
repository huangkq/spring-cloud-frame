package com.tech.base.utils.kafka;

import java.util.ArrayList;

public class EventDtoBatchBuild<T> extends ArrayList<T> {

    /****/
    private static final long serialVersionUID = -3754822561360910119L;

    @SuppressWarnings("unchecked")
    public EventDtoBatchBuild<T> send() {
        return KafkaUtil.send((EventDtoBatchBuild<? extends EventDtoBuild<?>>) this);
    }
}
