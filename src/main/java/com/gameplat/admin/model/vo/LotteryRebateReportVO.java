package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/** @Description : 彩票投注返点 @Author : cc @Date : 2022/3/21 */
@Data
public class LotteryRebateReportVO {

  @ApiModelProperty(value = "记录统计日期")
  private String countDate;

  @ApiModelProperty(value = "享返点的代理名称")
  private String userName;

  @ApiModelProperty(value = "代理层级")
  private String userLevel;

  @ApiModelProperty(value = "享返点的代理的上级名称  用于 转代理时 统计")
  private String parentName;

  @ApiModelProperty(value = "享返点的代理的代理路径 用于 转代理时 统计")
  private String agentPath;

  @ApiModelProperty(value = "返点来源用户名称")
  private String betUserName;

  @ApiModelProperty(value = "返点时他自己的点数")
  private Double betRate;

  @ApiModelProperty(value = "返点时 他直属下级的点数")
  private Double childBetRate;

  @ApiModelProperty(value = "此来源用户的投注总金额")
  private BigDecimal betAmount;

  @ApiModelProperty(value = "此来源用户的总输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "此来源用户的总有效投注金额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "此来源用户的总注单数")
  private Integer orderNum;

  @ApiModelProperty(value = "状态 0.未派发   1.已派发")
  private Integer status;

  @ApiModelProperty(value = "返水金额")
  private BigDecimal backWaterAmount;

  @ApiModelProperty(value = "返水公式")
  private String backWaterFormula;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "更新时间")
  private Date updateTime;
}
