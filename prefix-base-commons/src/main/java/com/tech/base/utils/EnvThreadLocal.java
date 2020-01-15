package com.tech.base.utils;

import com.tech.base.model.XEnvDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

public class EnvThreadLocal {

    private static final Logger logger = LoggerFactory.getLogger(EnvThreadLocal.class);

    private static final ThreadLocal<XEnvDto> headEnv = new NamedThreadLocal<>("headEnv context");

    /**
     * 绑定当前线程数据源路由的key 注意：入口aop线程才调用set方法，避免其他地方线程池复用的问题，请不要随意操作复值
     */
    public static void putXenv(XEnvDto env) {
        if (env != null) {
            clearCurrentThreadEnv();
        }
        headEnv.set(env);
    }

    /**
     * 获取当前线程的数据源路由的key
     */
    public static XEnvDto getEnv() {
        return headEnv.get();
    }

    public static void clearCurrentThreadEnv() {
        try {
            headEnv.remove();
        } catch (Exception e) {
            logger.error("clearCurrentThreadEnv error", e);
        }
    }
}
