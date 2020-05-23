package com.tech.base.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.base.interceptor.EnvInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "use.common.web", matchIfMissing = true)
public class WebConfiguration implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Value("${common.server.tomcat.contextPath:/}")
    private String contextPath;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Primary
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter webMessageConverter = mappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        webMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(webMessageConverter);
        logger.info("web http message converter init finish >> {} ", webMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!contextPath.equals("/")) {
            registry.addInterceptor(new EnvInterceptor())
            .addPathPatterns("/**").excludePathPatterns("/**/swagger-ui.html/**", "/**/webjars/**", "/**/swagger-resources/**", "/**/actuator/prometheus");
        } else {
            registry.addInterceptor(new EnvInterceptor())
            .addPathPatterns("/**").excludePathPatterns("/swagger-ui.html/**", "/webjars/**", "/swagger-resources/**", "/actuator/prometheus/**");
        }
    }

}
