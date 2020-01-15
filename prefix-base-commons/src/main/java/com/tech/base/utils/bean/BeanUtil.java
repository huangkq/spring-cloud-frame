package com.tech.base.utils.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.copier.CopyOptions;

public class BeanUtil {

    private static CopyOptions setIgnoreNullValue = CopyOptions.create().setIgnoreNullValue(true);

    /**
     * 说明:bean复制，返回目标对象,模板对象必须存在无参构造函数
     * 
     * @author huangkeqi
     * @param sourceObj 来源对象,如果来源对象是个集合类型，只支持List
     * @param targetClass 目标对象class
     * @param ignoreNullValue true排除null字段不复制
     * @return class T object
     * @date 2018年12月10日
     */
    public static <T> T beanCopy(Object sourceObj, Class<?> targetClass, boolean ignoreNullValue) {
        return ignoreNullValue ? beanCopy(sourceObj, targetClass, setIgnoreNullValue)
                : beanCopy(sourceObj, targetClass, CopyOptions.create().setIgnoreNullValue(false));
    }

    /**
     * 排除null值赋值
     * 
     * @param sourceObj
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T beanCopyIgnoreNullValue(Object sourceObj, Class<?> targetClass) {
        return beanCopy(sourceObj, targetClass, Boolean.TRUE);
    }

    @SuppressWarnings("unchecked")
    public static <T> T beanCopy(Object sourceObj, Class<?> targetClass, CopyOptions options) {
        if (sourceObj == null) return null;
        try {
            if (sourceObj instanceof List) {
                ArrayList<Object> arrayList = new ArrayList<>();
                ((List<?>) sourceObj).forEach((obj) -> {
                    try {
                        Object instanceCopy = instanceCopy(obj, targetClass, options);
                        arrayList.add(instanceCopy);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return (T) arrayList;
            } else {
                return (T) instanceCopy(sourceObj, targetClass, options);
            }
        } catch (Exception e) {
            throw new RuntimeException("beanCopy ERROR", e);
        }
    }

    private static Object instanceCopy(Object sourceObj, Class<?> targetClass, CopyOptions options) throws Exception {
        Object newInstance = targetClass.newInstance();
        copyProperties(sourceObj, newInstance, options);
        return newInstance;
    }

    /**
     * 说明:对象属性值复制
     * 
     * @author huangkeqi
     * @param sourceObj 源数据据对象
     * @param targetObj 复制给目据对象
     * @date 2018年12月13日
     */
    public static void copyProperties(Object sourceObj, Object targetObj) {
        copyProperties(sourceObj, targetObj, setIgnoreNullValue);
    }

    /**
     * 说明:对象属性值复制
     * 
     * @author huangkeqi
     * @param sourceObj 源数据据对象
     * @param targetObj 复制给目据对象
     * @param options CopyOptions
     * @date 2018年12月13日
     */
    public static void copyProperties(Object sourceObj, Object targetObj, CopyOptions options) {
        cn.hutool.core.bean.BeanUtil.copyProperties(sourceObj, targetObj, options);
    }

    public static Map<String, Object> objectToMap(Object obj) {
        return cn.hutool.core.bean.BeanUtil.beanToMap(obj);
    }

    /** 转换为下划线 */
    public static String underlineTocamel(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }


    /** 转换为驼峰 */
    public static String camelToUnderline(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}
