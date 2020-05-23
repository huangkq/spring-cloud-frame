package com.tech.base.config.init;

import com.tech.base.config.http.RestTemplateConfig;
import com.tech.base.config.jackson.JacksonConfig;
import com.tech.base.config.kafka.KafkaConfig;
import com.tech.base.config.redis.RedisConfig;
import com.tech.base.utils.http.RestTemplateUtil;
import com.tech.base.utils.kafka.KafkaUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@AutoConfigureAfter(value = {JacksonConfig.class, RestTemplateConfig.class, RedisConfig.class, KafkaConfig.class})
@Configuration
public class CommonInitializationConfiguration {

    @Autowired(required = false)
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        if (restTemplate != null) RestTemplateUtil.setRestTemplate(restTemplate);
        if (kafkaTemplate != null) KafkaUtil.setKafkaTemplate(kafkaTemplate);
    }

}
