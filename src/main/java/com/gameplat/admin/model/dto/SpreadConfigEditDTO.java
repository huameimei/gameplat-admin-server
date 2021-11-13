package com.gameplat.admin.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpreadConfigEditDTO {

    private Long id;
    /**
     * 代理账号
     */
    private String agentAccount;
    /**
     * 推广地址
     */
    private String externalUrl;
    /**
     * 推广码
     */
    private String code;
    /**
     * 推广类型
     */
    private Integer spreadType;
    /**
     * 推广用户类型
     */
    private Integer userType;
    /**
     * 用户层级
     */
    private Integer userLevel;
    /**
     * 彩票反水比率
     */
    private double rebate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 有效天数
     */
    private Integer effectiveDays;
    /**
     * 注册送彩金
     */
    private BigDecimal discountAmount;

}
