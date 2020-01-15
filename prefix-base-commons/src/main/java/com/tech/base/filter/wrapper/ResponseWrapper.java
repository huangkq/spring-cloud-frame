package com.tech.base.filter.wrapper;

import javax.servlet.ServletOutputStream;

import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.tech.base.response.RpcResponse;
import com.tech.base.utils.JacksonUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;



public class ResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream buffer;// 输出到byte array
    private ServletOutputStream out;
    private PrintWriter writer;

    public ResponseWrapper(HttpServletResponse resp) throws IOException {
        super(resp);
        buffer = new ByteArrayOutputStream();// 真正存储数据的流
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8.name()));
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return out;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    public byte[] getResponseData() throws IOException {
        flushBuffer();
        if (buffer.toByteArray() == null || buffer.toByteArray().length <= 0) {
            return JacksonUtil.getObjectMapper().writeValueAsBytes(new RpcResponse<>());
        } else {
            return buffer.toByteArray();
        }
    }

    private class WapperedOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos;

        public WapperedOutputStream(ByteArrayOutputStream stream) {
            bos = stream;
        }

        @Override
        public void write(int b) {
            bos.write(b);
        }

        @Override
        public void write(byte[] b) {
            bos.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {}
    }
}
