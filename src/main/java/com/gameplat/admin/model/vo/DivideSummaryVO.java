package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @Schema(description = "分红期数表主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long periodsId;

  @Schema(description = "分红代理的用户主键id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long userId;

  @Schema(description = "分红代理的名称")
  private String account;

  @Schema(description = "分红代理的层级")
  private Integer agentLevel;

  @Schema(description = "分红代理上级的用户id")
  private Long parentId;

  @Schema(description = "分红代理的上级")
  private String parentName;

  @Schema(description = "分红代理的代理路径")
  private String agentPath;

  @Schema(description = "汇总状态 1 已结算  2 已派发  3 部分派发(预留状态)")
  private Integer status;

  @Schema(description = "所有下级总的有效投注")
  private BigDecimal validAmount;

  @Schema(description = "所有下级总的输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "总分红金额")
  private BigDecimal divideAmount;

  @Schema(description = "真实总分红金额")
  private BigDecimal realDivideAmount;

  @Schema(description = "上期累计金额")
  private BigDecimal lastPeriodsAmount;

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

  private String periodsStartDate;
  private String periodsEndDate;
}
