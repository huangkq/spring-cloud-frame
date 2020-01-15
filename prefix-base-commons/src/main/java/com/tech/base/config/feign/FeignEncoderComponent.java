package com.tech.base.config.feign;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Request;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

@Component
public class FeignEncoderComponent implements Encoder {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructType(bodyType);
            template.body(Request.Body.encoded(objectMapper.writerFor(javaType).writeValueAsBytes(object), Charset.forName("utf-8")));
        } catch (JsonProcessingException e) {
            throw new EncodeException(e.getMessage(), e);
        }
    }
}
