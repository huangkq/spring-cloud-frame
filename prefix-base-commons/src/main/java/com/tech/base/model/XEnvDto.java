package com.tech.base.model;

import com.tech.base.annotation.XEnv;

import lombok.Data;

@Data
public class XEnvDto {

    @XEnv("x-trace-id")
    private String traceId;// 用于追踪同一个请求链，由顶层调用者传入

    @XEnv("x-token")
    private String token;// 安全key

    @XEnv("x-env-utm-source")
    private String utmSource;// 广告来源

    @XEnv("x-env-utm-medium")
    private String utmMedium;// 广告媒介

    @XEnv("x-env-utm-campaign")
    private String utmCampaign;// 广告名称

    @XEnv("x-env-utm-term")
    private String utmTerm;

    @XEnv("x-env-utm-content")
    private String utmContent;

    @XEnv("x-env-user-id")
    private Long userId;// 来源用户

    @XEnv("x-env-pk")
    private String pk;// 来源用户

    @XEnv("x-env-ps")
    private String ps;

    @XEnv("x-env-pd")
    private String pd;// 主从关系标示

    @XEnv("x-env-pp")
    private String pp;// 上级用户信息

    @XEnv("x-env-app-type")
    private Integer appType;// 1公众号,2ahaschool拼团小程序,3app,4小恐龙,5严选

    @XEnv("x-env-guniqid")
    private String guniqid;// 唯一用户表示

    @XEnv("x-env-channel")
    private String channel;// 应用市场，值定义：wandoujia,baidu,c360,xiaomi,yingyongbao,huawei,googleplay,oppo,vivo,meizu,guanwang,toutiao,baiduSEM,_test,applestore

    // ============其他http层标准属性==============
    @XEnv("x-forwarded-for")
    private String httpForwardedFor;

    @XEnv("user-agent")
    private String httpUserAgent;

    @XEnv("os")
    private String os;

    @XEnv("version")
    private String version;

    @XEnv("x-equipment")
    private String equipment;//设备类型 值定义:ipad iphone

    public XEnvDto() {
        super();
    }

    public String remoteIp() {
        return this.httpForwardedFor;
    }

}
