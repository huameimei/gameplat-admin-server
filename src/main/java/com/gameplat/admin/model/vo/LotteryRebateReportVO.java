package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/** @Description : 彩票投注返点 @Author : cc @Date : 2022/3/21 */
@Data
public class LotteryRebateReportVO {

  @Schema(description = "记录统计日期")
  private String countDate;

  @Schema(description = "享返点的代理名称")
  private String userName;

  @Schema(description = "代理层级")
  private String userLevel;

  @Schema(description = "享返点的代理的上级名称  用于 转代理时 统计")
  private String parentName;

  @Schema(description = "享返点的代理的代理路径 用于 转代理时 统计")
  private String agentPath;

  @Schema(description = "返点来源用户名称")
  private String betUserName;

  @Schema(description = "返点时他自己的点数")
  private Double betRate;

  @Schema(description = "返点时 他直属下级的点数")
  private Double childBetRate;

  @Schema(description = "此来源用户的投注总金额")
  private BigDecimal betAmount;

  @Schema(description = "此来源用户的总输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "此来源用户的总有效投注金额")
  private BigDecimal validAmount;

  @Schema(description = "此来源用户的总注单数")
  private Integer orderNum;

  @Schema(description = "状态 0.未派发   1.已派发")
  private Integer status;

  @Schema(description = "返水金额")
  private BigDecimal backWaterAmount;

  @Schema(description = "返水公式")
  private String backWaterFormula;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新时间")
  private Date updateTime;
}
