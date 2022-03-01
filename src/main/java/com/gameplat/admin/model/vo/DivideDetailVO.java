package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description : 分红详情VO
 * @Author : cc
 * @Date : 2022/2/26
 */
@Data
public class DivideDetailVO implements Serializable {

    private static final long serialVersionUID = 7535872776555957753L;

    @ApiModelProperty(value = "主键ID")
    @JsonSerialize(using = ToStringSerializer.class)
	private Long id;

    @ApiModelProperty(value = "分红期数表主键ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long periodsId;

    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "分红代理的用户主键id")
    private Long proxyId;

    @ApiModelProperty(value = "分红代理的名称")
    private String proxyName;

    @ApiModelProperty(value = "分红代理的层级")
    private Integer agentLevel;

    @ApiModelProperty(value = "分红代理的代理路径")
    private String superPath;

    @ApiModelProperty(value = "游戏大类code")
    private String liveCode;

    @ApiModelProperty(value = "游戏大类name")
    private String liveName;

    @ApiModelProperty(value = "一级游戏code")
    private String code;

    @ApiModelProperty(value = "一级游戏name")
    private String name;

    @ApiModelProperty(value = "此用户的有效投注")
    private BigDecimal validAmount;

    @ApiModelProperty(value = "此用户的输赢金额")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "结算方式 1 输赢金额 2  有效投注")
    private Integer settleType;

    @ApiModelProperty(value = "金额比例")
    private BigDecimal amountRatio;

    @ApiModelProperty(value = "分红代理所在此条代理线的分红比例")
    private BigDecimal divideRatio;

    @ApiModelProperty(value = "分红金额")
    private BigDecimal divideAmount;

    @ApiModelProperty(value = "分红公式")
    private String divideFormula;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "备注")
    private String remark;

}