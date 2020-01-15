package com.tech.base.constants;

public class ResponseConstants {

    public final static int SUCCESS = 0;

    public final static String SUCCESS_MSG = "ok";

    public final static int FAILURE = 1;

    public final static String FAILURE_MSG = "operation failure";

    // hystrix fallback default message
    public final static String HYSTRIX_FALLBACK_MSG = "接口调用异常,执行默认降级策略";

    public final static String AUTHENTICATION_FAILED_MSG = "该接口不对访客开放";

    public final static int AUTHENTICATION_FAILED_CODE = 500500;

    public final static int ERROR_CODE_1000 = 1000;// 交易失败
}
