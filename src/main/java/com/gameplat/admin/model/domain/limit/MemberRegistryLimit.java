package com.gameplat.admin.model.domain.limit;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册配置
 */
@Data
public class MemberRegistryLimit implements Serializable {

    /**
     * 是否开起注册功能1表示开启,2表示关闭
     */
    private Integer registrySwitch;
    /**
     * 真实姓名0表示可见，1表示必填，2表示隐藏
     */
    private Integer realName;
    /**
     * 电话0表示可见，1表示必填，2表示隐藏
     */
    private Integer phone;
    /**
     * 生日0表示可见，1表示必填，2表示隐藏
     */
    private Integer birthdate;

    /**
     * 邮箱0表示可见，1表示必填，2表示隐藏
     */
    private Integer email;

    /**
     * 微信0表示可见，1表示必填，2表示隐藏
     */
    private Integer weChat;

    /**
     * 推广1表示可见
     */
    private Integer intrCode;
    /**
     * QQ0表示可见，1表示必填，2表示隐藏
     */
    private Integer qq;

    /**
     * 取款密码0表示可见，1表示必填，2表示隐藏
     */
    private Integer withdrawalPassword;


    /**
     * 注册送彩金 40
     */
    private Double registrySendHandsel;

    /**
     * 注册需要彩金打码量
     */
    private Double regBonusCodeQuantity;

    /**
     * 试玩账号初始金额
     */
    private Double triAccountAmount;

    /**
     * 同一IP注册开启验证码次数
     */
    private Integer sameIpRegister;

    /**
     * 是否开起试玩用户验证码功能1表示开启,0表示关闭
     */
    private Integer trailUserValidCode;

    /**
     * 同IP限制注册用户数 0表示不限制
     */
    private Integer registerLimit;

    /**
     * 注册是否验证手机号0 关闭 1开启
     * */
    private Integer openCellphoneVerification;

    /**
     * 真实姓名1表示唯一
     */
    private Integer onlyfullName;

    /**
     * 唯一电话号码值为0表示可重复，1表示唯一
     */
    private Integer onlyfullPhone;
}
