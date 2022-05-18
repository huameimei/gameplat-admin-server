package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 分红详情VO @Author : cc @Date : 2022/2/26
 */
@Data
public class DivideDetailVO implements Serializable {

  private static final long serialVersionUID = 7535872776555957753L;

  @Schema(description = "主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @Schema(description = "分红期数表主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long periodsId;

  @Schema(description = "用户id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long userId;

  @Schema(description = "用户名")
  private String userName;

  @Schema(description = "分红代理的层级")
  private Integer agentLevel;

  @Schema(description = "分红代理的代理路径")
  private String superPath;

  @Schema(description = "用户类型")
  private String userType;

  @Schema(description = "分红代理的用户主键id")
  private Long proxyId;

  @Schema(description = "分红代理的名称")
  private String proxyName;

  @Schema(description = "游戏大类code")
  private String liveCode;

  @Schema(description = "游戏大类name")
  private String liveName;

  @Schema(description = "一级游戏code")
  private String code;

  @Schema(description = "一级游戏name")
  private String name;

  @Schema(description = "此用户的有效投注")
  private BigDecimal validAmount;

  @Schema(description = "此用户的输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "结算方式 1 输赢金额 2  有效投注")
  private Integer settleType;

  @Schema(description = "金额比例")
  private BigDecimal amountRatio;

  @Schema(description = "分红代理所在此条代理线的分红比例")
  private BigDecimal divideRatio;

  @Schema(description = "分红金额")
  private BigDecimal divideAmount;

  @Schema(description = "分红公式")
  private String divideFormula;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "备注")
  private String remark;
}
