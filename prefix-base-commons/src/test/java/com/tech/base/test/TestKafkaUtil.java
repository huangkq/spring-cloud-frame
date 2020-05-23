package com.tech.base.test;

import com.tech.base.model.XEnvDto;
import com.tech.base.utils.jackson.JacksonUtil;
import com.tech.base.utils.kafka.EventDtoBatchBuild;
import com.tech.base.utils.kafka.EventDtoBuild;
import com.tech.base.utils.kafka.KafkaUtil;


public class TestKafkaUtil {

    public static void main(String[] args) {
        EventDtoBuild<XEnvDto> event = KafkaUtil.buildEventMsg(new XEnvDto());
        String objToJson = JacksonUtil.objToJson(event);
        System.out.println(objToJson);

        EventDtoBatchBuild<EventDtoBuild<XEnvDto>> buildEventMsgBatch = KafkaUtil.buildEventMsgBatch();
        buildEventMsgBatch.add(event);
        buildEventMsgBatch.add(event);
        buildEventMsgBatch.add(event);
        
        System.out.println(JacksonUtil.objToJson(buildEventMsgBatch));

    }

}
