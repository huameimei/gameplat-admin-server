package com.gameplat.admin.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TokenInfo implements Serializable {
    // 用户id
    private Long uid;
    // token值
    private String token;
    // 登录时间
    private Date loginDate;
    // 最后操作时间
    private Date lastDate;
    // token有效时间
    private int expiresIn;

    private String loginIp;

    private String account;

    private String name;

    private Integer userType;

    private Double money;//余额

    /**
     * 设备类型
     */
    private String deviceType;

    private Date userAddTime;

}
