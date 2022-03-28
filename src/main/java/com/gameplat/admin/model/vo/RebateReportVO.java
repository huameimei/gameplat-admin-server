package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣报表 @Author : cc @Date : 2022/3/22 */
@Data
public class RebateReportVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "id")
  private Long reportId;

  @ApiModelProperty(value = "统计月份")
  private String countDate;

  @ApiModelProperty(value = "上级ID")
  private Long parentId;

  @ApiModelProperty(value = "上级账号")
  private String parentName;

  @ApiModelProperty(value = "代理ID")
  private Long agentId;

  @ApiModelProperty(value = "代理账号")
  private String agentName;

  @ApiModelProperty(value = "代理结构")
  private String agentPath;

  @ApiModelProperty(value = "代理层级")
  private Integer levelNum;

  @ApiModelProperty(value = "账号状态（1正常 0停用）")
  private Integer accountStatus;

  @ApiModelProperty(value = "下级会员")
  private Integer subMember;

  @ApiModelProperty(value = "有效会员")
  private Integer efficientMember;

  @ApiModelProperty(value = "下级代理")
  private Integer subAgent;

  @ApiModelProperty(value = "有效代理")
  private String efficientAgent;

  @ApiModelProperty(value = "方案ID")
  private Long planId;

  @ApiModelProperty(value = "方案名称")
  private String planName;

  @ApiModelProperty(value = "方案等级ID")
  private Long configId;

  @ApiModelProperty(value = "方案等级")
  private String rebateLevel;

  @ApiModelProperty(value = "公司总输赢（厅室输赢）")
  private BigDecimal gameWin;

  @ApiModelProperty(value = "下级会员佣金")
  private BigDecimal memberCommission;

  @ApiModelProperty(value = "下级会员佣金比例")
  private BigDecimal memberProportion;

  @ApiModelProperty(value = "下级代理佣金")
  private BigDecimal agentCommission;

  @ApiModelProperty(value = "下级代理佣金比例")
  private BigDecimal agentProportion;

  @ApiModelProperty(value = "下下级代理佣金")
  private BigDecimal subAgentCommission;

  @ApiModelProperty(value = "流水返利")
  private BigDecimal turnoverRebate;

  @ApiModelProperty(value = "流水返利")
  private BigDecimal turnoverProportion;

  @ApiModelProperty(value = "公司成本(红利、返水、平台费)")
  private BigDecimal totalCost;

  @ApiModelProperty(value = "累计负盈利")
  private BigDecimal negativeProfit;

  @ApiModelProperty(value = "佣金调整")
  private BigDecimal adjustmentAmount;

  @ApiModelProperty(value = "实际佣金")
  private BigDecimal actualCommission;

  @ApiModelProperty(value = "结算状态（0未结算 1风控审核 2财务审核 3已结算 4挂起）")
  private Integer status;

  @ApiModelProperty(value = "创建时间")
  private String createTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新时间")
  private String updateTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "备注")
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
