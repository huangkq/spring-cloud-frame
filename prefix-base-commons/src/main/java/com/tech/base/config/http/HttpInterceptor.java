package com.tech.base.config.http;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class HttpInterceptor implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

    private final static String HTTP_HEADER_CONNECTION_VALUE = "keep-alive";

    private final static String HTTP_HEADER_X_REQUESTED_WITH_KEY = "X-Requested-With";

    private final static String HTTP_HEADER_X_REQUESTED_WITH_VALUE = "XMLHttpRequest";

    private final static String HTTP_HEADER_KEEP_ALIVE_KEY = "Keep-Alive";

    private final static String HTTP_HEADER_KEEP_ALIVE_VALUE = "timeout=60";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        addHeader(request);
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        if (logger.isDebugEnabled()) {
            String requestLog = String.join(",", "httpInterceptor request  url:{}", request.getURI().toString(), //
                    "method:{}", request.getMethod().name(), //
                    "body:{}", new String(body, Charset.forName("UTF-8")), //
                    "headers:{}", request.getHeaders().toString()//
            );
            logger.debug(requestLog);
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {

        if (logger.isDebugEnabled()) {
            String responseLog = String.join(",", "httpInterceptor response statusCode:{}", String.valueOf(response.getStatusCode().value()), //
                    "statusText:{}", response.getStatusText(), //
                    "headers:{}", response.getHeaders().toString(), //
                    "response body: {}", StreamUtils.copyToString(response.getBody(), Charset.forName("UTF-8"))//
            );
            logger.debug(responseLog);
        }
    }

    /**
     * 拦截器添加Header 信息
     * 
     * @param request
     */
    private void addHeader(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();

        List<MediaType> acceptableMediaTypes = Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_OCTET_STREAM);


        if (!headers.containsKey(HttpHeaders.ACCEPT)) {
            headers.setAccept(acceptableMediaTypes);
        }

        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        // if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
        // headers.add(HttpHeaders.AUTHORIZATION, BASIC_AUTHORIZATION);
        // }

        if (!headers.containsKey(HttpHeaders.CONNECTION)) {
            headers.setConnection(HTTP_HEADER_CONNECTION_VALUE);
        }

        if (!headers.containsKey(HTTP_HEADER_X_REQUESTED_WITH_KEY)) {
            headers.add(HTTP_HEADER_X_REQUESTED_WITH_KEY, HTTP_HEADER_X_REQUESTED_WITH_VALUE);
        }

        if (!headers.containsKey(HTTP_HEADER_KEEP_ALIVE_KEY)) {
            headers.add(HTTP_HEADER_KEEP_ALIVE_KEY, HTTP_HEADER_KEEP_ALIVE_VALUE);
        }

        if (!headers.containsKey(HttpHeaders.CONTENT_ENCODING)) {
            headers.add(HttpHeaders.CONTENT_ENCODING, Charset.defaultCharset().name());
        }
    }

}
