package com.gameplat.admin.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SpreadLinkInfoAddDTO {

    /**
     * 代理Id
     */
    private Long agentId;
    /**
     * 代理账号
     */
    private String agentAccount;

    /**
     * 推广码
     */
    private String code;
    /**
     * 推广地址
     */
    private String externalUrl;
    /**
     * 推广类型
     */
    private Integer spreadType;
    /**
     * 推广用户类型
     */
    private Integer userType;
    /**
     * 是否专属 1 是  0 否
     */
    private Integer exclusiveFlag;
    /**
     * 用户层级
     */
    private Integer userLevel;
    /**
     * 彩票反水比率
     */
    private double rebate;
    /**
     * 有效天数
     */
    private Integer effectiveDays;
    /**
     * 注册送彩金
     */
    private BigDecimal discountAmount;
}
