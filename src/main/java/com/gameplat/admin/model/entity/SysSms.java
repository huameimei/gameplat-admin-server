package com.gameplat.admin.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysSms implements Serializable {

    private Long id;

    /**
     * 运营商 （1:阿里云 2:聚合）
     */
    private Integer operator;

    /**
     * 运营商名称
     */
    private String operatorName;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 验证码
     */
    private String validCode;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 发送状态 （0：发送成功 1：发送失败【失败原因参考message】 3已使用）
     */
    private Integer status;

    /**
     * 发送失败说明
     */
    private String message;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 短信类型 (1 登录短信  2  绑定手机号 3账户安全开启)
     */
    private Integer smsType;

    /**
     * 失效时间
     */
    private Date expireDate;

    /**
     * 请求ip
     */
    private String requestIp;

}
