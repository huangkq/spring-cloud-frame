package com.tech.base.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.base.utils.jackson.JacksonUtil;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(name = "use.common.jackson", matchIfMissing = true)
public class JacksonConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtil.getObjectMapper();
    }

}
