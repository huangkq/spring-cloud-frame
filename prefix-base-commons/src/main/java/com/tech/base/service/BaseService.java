package com.tech.base.service;

import com.tech.base.constants.BaseConstants;
import com.tech.base.exception.BaseExceptionCode;
import com.tech.base.exception.SystemException;
import com.tech.base.model.Response;
import com.tech.base.utils.ValidateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public abstract class BaseService {

    protected Logger logger;

    protected BaseService() {
        logger = LogManager.getLogger(getClass());
    }

    protected boolean isEmpty(Object obj) {
        return ValidateUtil.isEmpty(obj);
    }

    protected boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 断言:服务端不论成功失败返回统一json数据格式{code,message,data}</br>
     *
     * 断言:响应code=0才是交易可以用
     */
    protected <T> Optional<T> assertRpc(Response<T> rpcResponse) {
        if (rpcResponse == null) throw new SystemException(BaseExceptionCode.ERROR_CODE_100.getCode(), BaseExceptionCode.ERROR_CODE_100.getMsg());

        if (!rpcResponse.getCode().equals(BaseConstants.SUCCESS)) throw new SystemException(rpcResponse.getCode(), rpcResponse.getMessage());

        return Optional.ofNullable(rpcResponse.getData());
    }

}
