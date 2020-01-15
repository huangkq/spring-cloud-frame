package com.tech.base.exception.advice;

import com.tech.base.response.RpcResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

/**
 * 说明:SpringBoot 默认提供了一个全局的 handler 来处理所有的 HTTP 错误, 并把它映射为 /error. 当发生一个 HTTP 错误, 例如 404 错误时,
 * SpringBoot 内部的机制会将页面重定向到 /error 中.
 * 
 * @author huangkeqi date:2018年1月17日
 */
@RestController
public class HttpErrorHandler implements ErrorController {

    private final static String ERROR_PATH = "/error";

    private static Logger logger = LogManager.getLogger(HttpErrorHandler.class);

    /**
     * Supports other formats like JSON, XML
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public Object error(HttpServletRequest request) {
        Object attribute = request.getAttribute("javax.servlet.error.request_uri");
        String gurl404 = attribute != null ? attribute.toString() : null;
        if (StringUtils.isBlank(gurl404)) {
            Object[] objArr = (Object[]) getValueByFieldName(request, "specialAttributes");
            if (objArr != null && objArr.length > 6) {
                gurl404 = (String) objArr[6];
            }
        }
        logger.error("交易不存在url:{}", gurl404);
        return new RpcResponse<>(404, "访问接口不存在");
    }

    private Object getValueByFieldName(HttpServletRequest request, String string) {
        Object value = null;
        try {
            Class<?> xenvClazz = request.getClass();
            Field[] xenvClazzFields = xenvClazz.getDeclaredFields();
            for (Field field : xenvClazzFields) {
                if (field.getName().equals(string)) {
                    field.setAccessible(true);
                    value = field.get(request);
                    field.setAccessible(false);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Returns the path of the error page.
     * 
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
