package com.tech.base.config.http;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "use.common.http", matchIfMissing = true)
public class RestTemplateConfig {

    
    /**
     * restTemplate 使用jackson做httpMessageConverter 拦截器打印request和response的信息
     */
    @Primary
    @Bean
    public RestTemplate restTemplate(
            @Qualifier("httpComponentsClientHttpRequestFactory") HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory,
            @Qualifier("mappingJackson2HttpMessageConverter") MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        // 添加数据转换器,目前只支持string json
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("utf-8"));
        stringHttpMessageConverter.setSupportedMediaTypes(
                Arrays.asList(MediaType.APPLICATION_JSON, 
                        MediaType.APPLICATION_OCTET_STREAM, 
                        MediaType.TEXT_HTML, 
                        MediaType.TEXT_PLAIN));
        messageConverters.add(stringHttpMessageConverter);
        
        messageConverters.add(mappingJackson2HttpMessageConverter);

        messageConverters.add(new FormHttpMessageConverter());

        restTemplate.setMessageConverters(messageConverters);

        restTemplate.setInterceptors(Collections.singletonList(new HttpInterceptor()));
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory));
        return restTemplate;
    }
}
