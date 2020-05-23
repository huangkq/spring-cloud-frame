package com.tech.base.interceptor;

import com.tech.base.model.XEnvDto;
import com.tech.base.utils.EnvThreadLocal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EnvInterceptor implements HandlerInterceptor {

    private final static String X_TRACE_ID = "x-trace-id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        EnvThreadLocal.putXenv(getXEnvDto(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        EnvThreadLocal.clearCurrentThreadEnv();
    }

    public XEnvDto getXEnvDto(HttpServletRequest request) {
        XEnvDto xEnvDto = new XEnvDto();
        xEnvDto.setTraceId(getTraceId(request));
        xEnvDto.setToken(request.getHeader("x-token"));
        xEnvDto.setUtmSource(request.getHeader("x-env-utm-source"));
        xEnvDto.setUtmMedium(request.getHeader("x-env-utm-medium"));
        xEnvDto.setUtmCampaign(request.getHeader("x-env-utm-campaign"));
        xEnvDto.setUtmTerm(request.getHeader("x-env-utm-term"));
        xEnvDto.setUtmContent(request.getHeader("x-env-utm-content"));
        xEnvDto.setUserId(StringUtils.isBlank(request.getHeader("x-env-user-id")) ? null : Long.valueOf(request.getHeader("x-env-user-id")));
        xEnvDto.setPk(request.getHeader("x-env-pk"));
        xEnvDto.setPs(request.getHeader("x-env-ps"));
        xEnvDto.setPd(request.getHeader("x-env-pd"));
        xEnvDto.setPp(request.getHeader("x-env-pp"));
        xEnvDto.setAppType(StringUtils.isBlank(request.getHeader("x-env-app-type")) ? null : Integer.valueOf(request.getHeader("x-env-app-type")));
        xEnvDto.setGuniqid(request.getHeader("x-env-guniqid"));
        xEnvDto.setChannel(request.getHeader("x-env-channel"));
        xEnvDto.setHttpForwardedFor(request.getHeader("x-forwarded-for"));
        xEnvDto.setHttpUserAgent(request.getHeader("user-agent"));
        xEnvDto.setOs(request.getHeader("os"));
        xEnvDto.setVersion(request.getHeader("version"));
        xEnvDto.setEquipment(request.getHeader("x-equipment"));
        return xEnvDto;
    }

    private String getTraceId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(X_TRACE_ID)).orElse(getRandomTraceId());
    }

    private String getRandomTraceId() {
        return UUID.randomUUID().toString();
    }
}
