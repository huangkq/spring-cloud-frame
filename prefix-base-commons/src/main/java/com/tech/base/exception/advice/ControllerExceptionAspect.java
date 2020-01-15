package com.tech.base.exception.advice;

import com.tech.base.constants.ResponseConstants;
import com.tech.base.exception.BaseException;
import com.tech.base.filter.wrapper.RequestWrapper;
import com.tech.base.response.RpcResponse;
import com.tech.base.utils.EnvThreadLocal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * 接收controller层未捕获的异常
 */
@ControllerAdvice
public class ControllerExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionAspect.class);

    // * RestController, 表明 GlobalExceptionHandler 是一个 RESTful Controller, 即它会以 RESTful 的形式返回回复.
    // * ControllerAdvice, 表示 GlobalExceptionHandler 是一个全局的异常处理器.
    // 在 GlobalExceptionHandler 中, 我们使用了 ExceptionHandler 注解标注了两个方法:
    // * ExceptionHandler(value = BaseException.class): 表示 baseErrorHandler 处理 BaseException
    // 异常和其子异常.
    // * ExceptionHandler(value = Exception.class): 表示 defaultErrorHandler 会处理 Exception 异常和其所用子异常.
    // 要注意的是, 和 try...catch 语句块, 异常处理的顺序也是从具体到一般, 即如果 baseErrorHandler 可以处理此异常, 则调用此方法来处理异常, 反之使用
    // defaultErrorHandler 来处理异常
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Object errorHandler(HttpServletRequest request, Throwable ex) throws Exception {
        StringBuilder msg = logRequest((RequestWrapper) request);
        try {
            if (ex instanceof BaseException) {
                BaseException bex = (BaseException) ex;
                logger.warn("交易失败:{}", msg, ex);// warn 级别异常
                return new RpcResponse<>(bex.getCode(), bex.getMessage());
            } else {
                logger.error("交易失败:{}", msg, ex); // error
                return new RpcResponse<>(ResponseConstants.ERROR_CODE_1000, "交易失败");
            }
        } finally {
            EnvThreadLocal.clearCurrentThreadEnv();// 释放
        }
    }

    private StringBuilder logRequest(final RequestWrapper request) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("traceId=").append(request.getTraceId()).append(";");
        msg.append("method=").append(request.getMethod()).append(";");
        msg.append("url=").append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            msg.append('?').append(request.getQueryString());
        }
        msg.append(";body=").append(request.getBody());
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            msg.append(";headers:{");
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                msg.append(name).append("=").append(request.getHeader(name)).append(",");
            }
            msg.append("}");
        }
        return msg;
    }
}
