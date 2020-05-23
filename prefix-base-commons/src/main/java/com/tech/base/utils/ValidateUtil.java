package com.tech.base.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/**
 * 说明:验证工具类
 * 
 * @author huangkeqi
 * @date 2020年5月22日
 */
public class ValidateUtil {

    public static void assertTrue(boolean isAssert, String message) {
        if (isAssert) throw new RuntimeException(message);
    }

    /** 判断对象==null */
    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    /** 判断对象!=null */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /** 绝对空判断 对象==null 数值型==0 字符型="   " */
    public static boolean isEmpty(Object obj) {
        if (isNull(obj)) return true;
        Class<? extends Object> class1 = obj.getClass();
        if (class1.isArray()) {
            return arrayIsEmpty(obj);
        }
        String name = class1.getName();
        switch (name) {
            case "java.lang.String":
                return StringUtils.isBlank((String) obj);
            case "java.lang.Long":
                return ((Long) obj).longValue() <= 0L;
            case "java.lang.Integer":
                return ((Integer) obj).intValue() <= 0;
            case "java.lang.Byte":
                return ((Byte) obj).byteValue() <= 0;
            case "java.lang.Double":
                return ((Double) obj).doubleValue() <= 0.0;
            case "java.math.BigDecimal":
                return ((BigDecimal) obj).compareTo(BigDecimal.ZERO) <= 0;
            case "java.util.List":
            case "java.util.ArrayList":
            case "java.util.Arrays$ArrayList":
                return CollectionUtils.isEmpty((Collection<?>) obj);
            default:
                return isNull(obj);
        }
    }

    private static boolean arrayIsEmpty(Object array) {
        return ((Object[]) (array)).length == 0;
    }

    /** 绝对非空判断 对象!=null 数值型!=0 字符型!="   " */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断obj在objs中存在
     */
    public static boolean contains(Object obj, Object... objs) {
        if (obj == null) return false;

        if (objs == null || objs.length <= 0) return false;

        for (int i = 0; i < objs.length; i++) {
            if (Objects.equals(obj, objs[i])) return true;
        }
        return false;
    }
}
