package com.tech.base.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(name = "use.common.redis", matchIfMissing = false)
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${common.redis.host:localhost}")
    public String host;

    @Value("${common.redis.port:6379}")
    public Integer port;

    @Value("${common.redis.password}")
    public String password;

    @Value("${common.redis.timeout:10000}")
    public Integer timeout;

    @Value("${common.redis.database:0}")
    public Integer database;

    @Value("${common.redis.max-active:50}")
    public Integer maxActive;

    @Value("${common.redis.max-wait:10}")
    public Integer maxWait;

    @Value("${common.redis.max-idle:15}")
    public Integer maxIdle;

    @Value("${common.redis.min-idle:5}")
    public Integer minIdle;

    @Primary
    @Bean
    public <T> RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(getJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance);
        // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    @Primary
    @Bean(name = "redisConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = lettuceClientConfiguration(maxActive, maxIdle, minIdle, maxWait);
        RedisStandaloneConfiguration redisStandaloneConfiguration = redisStandaloneConfiguration(host, port, password, database);

        return createLettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    private LettuceClientConfiguration lettuceClientConfiguration(Integer maxActive, Integer maxIdle, Integer minIdle, Integer maxWait) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder().poolConfig(config);
        LettuceClientConfiguration clientConfig = builder.build();
        logger.info("lettuceClient配置加载完成 maxActive : {} , maxIdle : {} , minIdle : {} , maxWait : {}", maxActive, maxIdle, minIdle, maxWait);
        return clientConfig;
    }

    private RedisStandaloneConfiguration redisStandaloneConfiguration(String host, Integer port, String password, Integer database) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(database);
        logger.info("redis数据源加载完成 host : {}, port : {} database : {}", host, port, database);
        return config;
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration,
            LettuceClientConfiguration clientConfiguration) {
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
    }

}
