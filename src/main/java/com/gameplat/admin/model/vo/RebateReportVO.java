package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣报表 @Author : cc @Date : 2022/3/22 */
@Data
public class RebateReportVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "id")
  private Long reportId;

  @Schema(description = "统计月份")
  private String countDate;

  @Schema(description = "上级ID")
  private Long parentId;

  @Schema(description = "上级账号")
  private String parentName;

  @Schema(description = "代理ID")
  private Long agentId;

  @Schema(description = "代理账号")
  private String agentName;

  @Schema(description = "代理结构")
  private String agentPath;

  @Schema(description = "代理层级")
  private Integer levelNum;

  @Schema(description = "账号状态（1正常 0停用）")
  private Integer accountStatus;

  @Schema(description = "下级会员")
  private Integer subMember;

  @Schema(description = "有效会员")
  private Integer efficientMember;

  @Schema(description = "下级代理")
  private Integer subAgent;

  @Schema(description = "有效代理")
  private String efficientAgent;

  @Schema(description = "方案ID")
  private Long planId;

  @Schema(description = "方案名称")
  private String planName;

  @Schema(description = "方案等级ID")
  private Long configId;

  @Schema(description = "方案等级")
  private String rebateLevel;

  @Schema(description = "公司总输赢（厅室输赢）")
  private BigDecimal gameWin;

  @Schema(description = "下级会员佣金")
  private BigDecimal memberCommission;

  @Schema(description = "下级会员佣金比例")
  private BigDecimal memberProportion;

  @Schema(description = "下级代理佣金")
  private BigDecimal agentCommission;

  @Schema(description = "下级代理佣金比例")
  private BigDecimal agentProportion;

  @Schema(description = "下下级代理佣金")
  private BigDecimal subAgentCommission;

  @Schema(description = "流水返利")
  private BigDecimal turnoverRebate;

  @Schema(description = "流水返利")
  private BigDecimal turnoverProportion;

  @Schema(description = "公司成本(红利、返水、平台费)")
  private BigDecimal totalCost;

  @Schema(description = "累计负盈利")
  private BigDecimal negativeProfit;

  @Schema(description = "佣金调整")
  private BigDecimal adjustmentAmount;

  @Schema(description = "实际佣金")
  private BigDecimal actualCommission;

  @Schema(description = "结算状态（0未结算 1风控审核 2财务审核 3已结算 4挂起）")
  private Integer status;

  @Schema(description = "创建时间")
  private String createTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新时间")
  private String updateTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "备注")
  private String remark;

  public RebateReportVO() {
    this.efficientMember = 0;
    this.gameWin = BigDecimal.ZERO;
    this.totalCost = BigDecimal.ZERO;
    this.negativeProfit = BigDecimal.ZERO;
    this.memberProportion = BigDecimal.ZERO;
    this.agentCommission = BigDecimal.ZERO;
    this.adjustmentAmount = BigDecimal.ZERO;
    this.actualCommission = BigDecimal.ZERO;
  }

  public RebateReportVO(String countDate) {
    this.countDate = countDate;
    this.subMember = 0;
    this.efficientMember = 0;
    this.gameWin = BigDecimal.ZERO;
    this.totalCost = BigDecimal.ZERO;
    this.negativeProfit = BigDecimal.ZERO;
    this.memberProportion = BigDecimal.ZERO;
    this.agentCommission = BigDecimal.ZERO;
    this.adjustmentAmount = BigDecimal.ZERO;
    this.actualCommission = BigDecimal.ZERO;
  }
}
