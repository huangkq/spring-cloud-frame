package com.tech.base.utils.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 说明: json转换工具
 * 
 * @author huangkeqi
 * @date 2019年10月28日
 */
public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static TimeZone defaultTimezone = TimeZone.getTimeZone("GMT+8");

    private static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    private static ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(defaultTimezone);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_FULL));
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Instant.class,
                new InstantCustomSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT_FULL, Locale.SIMPLIFIED_CHINESE)));
        javaTimeModule.addDeserializer(Instant.class, new InstantCustomDeserializer());

        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT_FULL, Locale.SIMPLIFIED_CHINESE)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT_FULL, Locale.SIMPLIFIED_CHINESE)));

        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT_SHORT, Locale.SIMPLIFIED_CHINESE)));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT_SHORT, Locale.SIMPLIFIED_CHINESE)));

        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DATE_FORMAT_FULL)));
        javaTimeModule.addDeserializer(Date.class, new DateCustomDeserializer());

        objectMapper.registerModule(javaTimeModule);
        logger.info("json序列化初始化完成 [jackson] dateFormat : {} , namingStrategy : {} ", DATE_FORMAT_FULL, objectMapper.getPropertyNamingStrategy());
    }

    /** 对象转JSON */
    public static String objToJson(Object data) throws RuntimeException {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转json数据格式失败!", e);
        }
    }

    /**
     * 说明: JSON转对象 支持泛型
     * <p>
     * 普通转换例子：OrderVo orderVo2 =JacksonUtil.jsonToObject(objToJson2, OrderVo.class);
     * <p>
     * 泛型转换例子：EventMsg&lt;OrderVo&gt; buildEventMsg = JacksonUtil.jsonToObject(objToJson,EventMsg.class, OrderVo.class);
     * 
     * @param json
     * @param collectionClass 转换对象class
     * @param clazzs 不是泛型不需要传
     * @return T
     * @throws RuntimeException
     * @date: 2019年10月28日
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String json, Class<?> collectionClass, Class<?>... clazzs) throws RuntimeException {
        try {
            JavaType collectionType = getCollectionType(collectionClass, clazzs);
            return (T) objectMapper.readValue(json, collectionType);
        } catch (Exception e) {
            throw new RuntimeException("json数据格式不正确,或转换失败!", e);
        }
    }

    /** JSON转集合对象 */
    public static <T> T jsonToList(String json, Class<?>... elementClasses) throws RuntimeException {
        return jsonToObject(json, ArrayList.class, elementClasses);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
