package com.gameplat.admin.model.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sport")
public class SportProperty {

    //请求主域名
    private String domain;

    //直播域名列表
    private String liveDomainList;

    //直播数据统计
    private String LiveDomainTraffic;
}
