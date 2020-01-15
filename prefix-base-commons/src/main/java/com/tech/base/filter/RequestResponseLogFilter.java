package com.tech.base.filter;

import com.tech.base.filter.wrapper.RequestWrapper;
import com.tech.base.filter.wrapper.ResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(1)
@Component
@WebFilter(filterName = "RequestResponseLogFilter", urlPatterns = "/*")
public class RequestResponseLogFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLogFilter.class);

    private static final String REQUEST_PREFIX = "请求:";
    private static final String RESPONSE_PREFIX = "响应:";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        request = new RequestWrapper(request);
        response = new ResponseWrapper(response);
        if (!skipLogging(request.getRequestURL().toString())) {
            filterChain.doFilter(request, response);
        } else {
            StringBuilder logRequest = logRequest((RequestWrapper) request);
            filterChain.doFilter(request, response);
            logResponse((ResponseWrapper) response, logRequest, ((RequestWrapper) request).getStartMillis());
            logger.info(logRequest.toString());
        }
    }

    private StringBuilder logRequest(final RequestWrapper request) throws IOException {
        StringBuilder msg = new StringBuilder(REQUEST_PREFIX);
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

    private void logResponse(ResponseWrapper response, StringBuilder msg, long startMillis) throws IOException {
        msg.append(RESPONSE_PREFIX);
        if (logger.isDebugEnabled()) {
            msg.append(";");
            msg.append(RESPONSE_PREFIX);
            msg.append(";response=").append(new String(response.getResponseData(), StandardCharsets.UTF_8));
        }
        msg.append(";处理时间:").append((System.currentTimeMillis() - startMillis));
    }

    /**
     * 是否跳过日志输出
     * 
     * @param uri
     * @return
     */
    private Boolean skipLogging(String uri) {
        return uri.contains("prometheus")//
                || uri.contains("webjars") //
                || uri.contains("swagger") //
                || uri.contains("api-docs")//
                || uri.contains("favicon");
    }

}


