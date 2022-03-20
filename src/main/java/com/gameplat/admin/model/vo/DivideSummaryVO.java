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
 * @Description : 分红汇总VO @Author : cc @Date : 2022/2/26
 */
@Data
public class DivideSummaryVO implements Serializable {

  private static final long serialVersionUID = 7535872776555957753L;

  @ApiModelProperty(value = "主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @ApiModelProperty(value = "分红期数表主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long periodsId;

  @ApiModelProperty(value = "分红代理的用户主键id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long userId;

  @ApiModelProperty(value = "分红代理的名称")
  private String account;

  @ApiModelProperty(value = "分红代理的层级")
  private Integer agentLevel;

  @ApiModelProperty(value = "分红代理上级的用户id")
  private Long parentId;

  @ApiModelProperty(value = "分红代理的上级")
  private String parentName;

  @ApiModelProperty(value = "分红代理的代理路径")
  private String agentPath;

  @ApiModelProperty(value = "汇总状态 1 已结算  2 已派发  3 部分派发(预留状态)")
  private Integer status;

  @ApiModelProperty(value = "所有下级总的有效投注")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "所有下级总的输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "总分红金额")
  private BigDecimal divideAmount;

  @ApiModelProperty(value = "真实总分红金额")
  private BigDecimal realDivideAmount;

  @ApiModelProperty(value = "上期累计金额")
  private BigDecimal lastPeriodsAmount;

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

  private String periodsStartDate;
  private String periodsEndDate;
}
