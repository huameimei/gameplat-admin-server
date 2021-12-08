package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;

/**
 * 日志Vo
 * @author three
 */
@Data
public class LogVO {

    /**
     * 账号
     */
    private String userName;
    /**
     * 真是姓名
     */
    private String realName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户等级
     */
    private String userLevel;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 浏览器代理
     */
    private String userAgent;
    /**
     * 操作域名
     */
    private String domain;
    /**
     * 登录ip
     */
    private String ipAddress;
    /**
     * ip归属地
     */
    private String ipDesc;
    /**
     * 创建时间
     */
    private Date createTime;

}
