package com.tech.base.utils.kafka;

import com.tech.base.model.EventDto;
import com.tech.base.utils.jackson.JacksonUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * 说明: 发送kafka新版消息event 工具类
 * 
 * @author huangkeqi date: 2019年10月28日
 */
public class KafkaUtil {
    private static Logger logger = LoggerFactory.getLogger(KafkaUtil.class);

    private static KafkaTemplate<String, Object> kafkaTemplate;

    public static KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    /**
     * 说明: 自行初始化对象
     */
    public static void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        KafkaUtil.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 说明:发送消息数据
     * 
     * @param data
     * @throws RuntimeException
     * @date: 2019年10月28日
     */
    public static void send(EventDto<?> data, String topic) throws RuntimeException {
        if (StringUtils.isBlank(topic)) throw new RuntimeException("topic required");
        try {
            doSend(topic, null, JacksonUtil.objToJson(data));
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable ex) {
            logger.error("[消息发送]发送数据失败sendMessage:{}", data.toString(), ex);
            throw new RuntimeException("[消息发送]回调响应出现异常uuid:" + data.getUniqueSerial());
        }
    }

    // 发送
    private static void doSend(String topic, String key, String sendMsg) {
        org.springframework.util.concurrent.ListenableFuture<SendResult<String, Object>> send = kafkaTemplate.send(topic, key, sendMsg);
        AtomicBoolean isOK = new AtomicBoolean(true);
        // 回调监听
        send.addCallback(result -> {
            // 成功
        }, ex -> {
            isOK.set(false);
            // 异常
            logger.error("[消息发送]发送数据失败sendMessage:{}", sendMsg, ex);
        });
        kafkaTemplate.flush();// 等待确认数据推送到服务端
        if (!isOK.get()) {
            throw new RuntimeException("[消息发送]FailureCallback异常:" + sendMsg);
        }
    }

    /**
     * 说明:批量发送
     * 
     * @param eventMsgBatch
     * @return 发送失败的数据sendFailList date:2019年10月25日
     */
    @SuppressWarnings("unchecked")
    public static <T> T send(EventDtoBatchBuild<? extends EventDtoBuild<?>> eventMsgBatch) {
        EventDtoBatchBuild<EventDtoBuild<?>> sendFailList = new EventDtoBatchBuild<>();// 保存失败的数据
        eventMsgBatch.forEach(data -> {
            try {
                data.check();
                String sendMsg = JacksonUtil.objToJson(data);
                logger.info("[消息发送]发送数据key:{},sendMsg:{}", data.getKey(), sendMsg);
                org.springframework.util.concurrent.ListenableFuture<SendResult<String, Object>> send =
                        kafkaTemplate.send(data.getTopic(), data.getKey(), sendMsg);
                // 回调监听
                send.addCallback((SendResult<String, Object> result) -> {
                    // 成功
                }, (Throwable ex) -> { // 异常
                    sendFailList.add(data);
                    logger.error("[消息发送]发送数据失败sendMessage:{}", sendMsg, ex);
                });
            } catch (Exception e) {
                logger.error("[消息发送]发送数据异常sendMsg:{},error:{}", JacksonUtil.objToJson(data), e.getMessage(), e);
                sendFailList.add(data);
            }
        });
        try {
            kafkaTemplate.flush();// flush 内部会一直等待30秒(CountDownLatch)，消息完成发送抵达
        } catch (Exception e) {
            logger.error("send kafka error:{}", e.getMessage(), e);
            return (T) eventMsgBatch;
        }
        return (T) sendFailList;
    }

    /**
     * 构造消息体对象，链式添加属性，和发送
     */
    public static <T> EventDtoBuild<T> buildEventMsg(T t) {
        return new EventDtoBuild<>(t);
    }

    /** 构造批量消息对象 */
    public static <T> EventDtoBatchBuild<EventDtoBuild<T>> buildEventMsgBatch() {
        return new EventDtoBatchBuild<>();
    }
}
