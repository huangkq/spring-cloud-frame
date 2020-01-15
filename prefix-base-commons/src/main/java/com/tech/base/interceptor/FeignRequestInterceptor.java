package com.tech.base.interceptor;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRequestInterceptor implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    public static final String CONNECTION = "Connection";

    private static final String HTTP_HEADER_CONNECTION_VALUE = "keep-alive";

    private static final String HTTP_HEADER_X_REQUESTED_WITH_KEY = "X-Requested-With";

    private static final String HTTP_HEADER_X_REQUESTED_WITH_VALUE = "XMLHttpRequest";

    private static final String HTTP_HEADER_KEEP_ALIVE_KEY = "Keep-Alive";

    private static final String HTTP_HEADER_KEEP_ALIVE_VALUE = "timeout=60";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String CHARSET_ENCODING = StandardCharsets.UTF_8.displayName();

    public static final String ACCEPT = "accept";


    @Override
    public void apply(RequestTemplate requestTemplate) {
        initRequestHeader(requestTemplate);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        copyOriginalRequestHeader(attributes, requestTemplate);
        feignRequestLogging(requestTemplate);
    }

    /**
     * 复制原始请求头
     * 
     * @param attributes
     * @param requestTemplate
     */
    private void copyOriginalRequestHeader(ServletRequestAttributes attributes, RequestTemplate requestTemplate) {
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String k = headerNames.nextElement();
            String v = request.getHeader(k);
            requestTemplate.header(k, v);
        }
    }

    /**
     * 初始化请求头
     * 
     * @param requestTemplate
     */
    private void initRequestHeader(RequestTemplate requestTemplate) {
        requestTemplate.header(CONTENT_TYPE, APPLICATION_JSON_UTF8);
        List<String> acceptableMediaTypes = Arrays.asList(MediaType.APPLICATION_JSON.toString(), MediaType.APPLICATION_XML.toString(),
                MediaType.TEXT_PLAIN.toString(), MediaType.APPLICATION_FORM_URLENCODED.toString(), MediaType.APPLICATION_OCTET_STREAM.toString());

        requestTemplate.header(ACCEPT, acceptableMediaTypes);
        requestTemplate.header(CONNECTION, HTTP_HEADER_CONNECTION_VALUE);
        requestTemplate.header(HTTP_HEADER_KEEP_ALIVE_KEY, HTTP_HEADER_KEEP_ALIVE_VALUE);
        requestTemplate.header(HTTP_HEADER_X_REQUESTED_WITH_KEY, HTTP_HEADER_X_REQUESTED_WITH_VALUE);
        requestTemplate.header(CONTENT_ENCODING, CHARSET_ENCODING);
    }

    /**
     * feign调用日志
     * 
     * @param requestTemplate
     */
    private void feignRequestLogging(RequestTemplate requestTemplate) {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Feign request URI : ").append(requestTemplate.url()).append(System.lineSeparator());
        sb.append("Feign request HEADER : ").append(requestTemplate.headers().toString()).append(System.lineSeparator());
        String body =
                requestTemplate.requestBody() == null ? Strings.EMPTY :
                    new String(requestTemplate.requestBody().asBytes(), Charset.forName("utf-8"));
        sb.append("Feign request BODY : ").append(body);

        logger.debug("Feign request Info : {}", sb);
    }
}
