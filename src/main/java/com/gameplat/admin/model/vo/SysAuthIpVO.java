package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SysAuthIpVO {

    /**
     * IP白名单
     */
    private String allowIp;

    /**
     * 类型
     */
    private String ipType;

    private Date createTime;

    private String createBy;

    private String remark;
}
