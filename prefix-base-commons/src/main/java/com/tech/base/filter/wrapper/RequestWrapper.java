package com.tech.base.filter.wrapper;


import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {

    private final static String X_TRACE_ID = "x-trace-id";
    private final byte[] body;
    private String traceId;
    private long startMillis;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.traceId = request.getHeader(X_TRACE_ID);
        this.body = IOUtils.toByteArray(request.getInputStream());
        this.startMillis = System.currentTimeMillis();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {}

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    public String getTraceId() {
        return traceId;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public String getBody() {
        return new String(body, StandardCharsets.UTF_8);
    }
}
