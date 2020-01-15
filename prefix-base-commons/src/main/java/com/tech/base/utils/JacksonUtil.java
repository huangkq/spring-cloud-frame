package com.tech.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明: json转换工具
 * @author huangkeqi
 * date:2019年10月28日
 */
public class JacksonUtil {

    private static ObjectMapper mapper;
    
    /**
     * 说明: 自行定制 ObjectMapper
     * @param mapper
     * date:2019年10月28日
     */
    public static void setMapper(ObjectMapper mapper) {
        JacksonUtil.mapper = mapper;
    }

    /** 对象转JSON */
    public static String objToJson(Object data) throws RuntimeException {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转json数据格式失败!", e);
        }
    }

    /**
     * 说明: JSON转对象 支持泛型
     * <p>
     * 普通转换例子：OrderVo orderVo2  =JacksonUtil.jsonToObject(objToJson2, OrderVo.class);
     * <p>
     *  泛型转换例子：EventMsg&lt;OrderVo&gt; buildEventMsg2 = JacksonUtil.jsonToObject(objToJson, EventMsg.class, OrderVo.class);
     * @param json 
     * @param collectionClass 转换对象class
     * @param clazzs 不是泛型不需要传
     * @return 
     * @throws RuntimeException
     * @date: 2019年10月28日
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String json, Class<?> collectionClass, Class<?> ... clazzs) throws RuntimeException {
        try {
            JavaType collectionType = getCollectionType(collectionClass, clazzs);
            return (T) mapper.readValue(json, collectionType);
        } catch (Exception e) {
            throw new RuntimeException("json数据格式不正确,或转换失败!", e);
        }
    }

    /** JSON中的某个字段的值 */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String json, String key, Class<?> clazz) throws RuntimeException {
        if (json == null || json.trim().length() == 0) {
            return null;
        }
        try {
            Map<String, Object> map = jsonToObject(json, HashMap.class);
            Object val = map.get(key);
            if (val == null) {
                return null;
            }
            String subJson = mapper.writeValueAsString(val);
            return (T) jsonToObject(subJson, clazz);
        } catch (Exception e) {
            throw new RuntimeException("json数据格式不正确,或转换失败!", e);
        }
    }
    
    /** JSON转集合对象 */
    public static <T> T jsonToList(String json, Class<?>... elementClasses) throws RuntimeException {
        return jsonToObject(json, ArrayList.class, elementClasses);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

}
