package com.tech.base.utils.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * 说明:rest调用工具类
 *
 * @author huangkeqi
 * @date 2017年4月20日
 */
public class RestTemplateUtil {
    private static Logger logger = LogManager.getLogger(RestTemplateUtil.class);

    private static RestTemplate restTemplate;

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public static void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateUtil.restTemplate = restTemplate;
    }

    public static RestBuild<?> buildtRest() {
        return new RestBuild<>();
    }

    public static RestBuild<?> buildRest(RequestEntity<Object> requestEntity) {
        return new RestBuild<>(requestEntity);
    }

    public static RestBuild<?> buildtRest(HttpHeaders httpHeaders) {
        return new RestBuild<>(httpHeaders);
    }

    static class RestBuild<T> {
        private RequestEntity<Object> requestEntity;
        private Class<T> classT;
        private HttpHeaders httpHeaders;
        private Object body;
        private HttpMethod httpMethod;
        private String url;

        private ResponseEntity<T> responseEntity;

        public RestBuild() {}

        public RestBuild(RequestEntity<Object> requestEntity) {
            this.requestEntity = requestEntity;
        }

        public RestBuild(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
        }

        public RestBuild<T> withResponseType(Class<T> responseType) {
            this.classT = responseType;
            return this;
        }

        public RestBuild<T> withHttpHeaders(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public RestBuild<T> withBody(Object body) {
            this.body = body;
            return this;
        }

        public RestBuild<T> withUrl(String url) {
            this.url = url;
            return this;
        }

        public RestBuild<T> withHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public ResponseEntity<T> sendResponseEntity() {
            if (requestEntity != null) {
                this.responseEntity = exchange(requestEntity);
            } else {
                this.responseEntity = exchange(new RequestEntity<>(this.body, this.httpHeaders, this.httpMethod, URI.create(this.url)));
            }
            return responseEntity;
        }

        public T send() {
            sendResponseEntity();
            return this.responseEntity.getBody();
        }

        private ResponseEntity<T> exchange(RequestEntity<Object> requestEntity) {
            return RestTemplateUtil.getRestTemplate().exchange(requestEntity, this.classT);
        }
    }
}


