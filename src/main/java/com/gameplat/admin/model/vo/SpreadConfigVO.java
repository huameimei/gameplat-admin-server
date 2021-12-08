package com.gameplat.admin.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class SpreadConfigVO {

    private Long id;
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
     * 是否专属类型
     */
    private Integer exclusiveFlag;
    /**
     * 推广用户类型
     */
    private Integer userType;
    /**
     * 用户层级
     */
    private Integer userLevel;
    /**
     * 有效天数
     */
    private Integer effectiveDays;
    /**
     * 访问数
     */
    private Integer visitCount;
    /**
     * 注册数
     */
    private Integer registCount;
    /**
     * 彩票反水比率
     */
    private double rebate;
    /**
     * 注册送彩金
     */
    private BigDecimal discountAmount;
    /**
     * 状态
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
}
