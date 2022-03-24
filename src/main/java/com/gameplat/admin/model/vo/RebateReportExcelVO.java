package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣报表 @Author : cc @Date : 2022/3/23 */
@Data
public class RebateReportExcelVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Excel(name = "统计月份", isImportField = "true_st", isWrap = false)
  private String countDate;

  @Excel(name = "上级账号", width = 15, isImportField = "true_st", isWrap = false)
  private String parentName;

  @Excel(name = "代理账号", width = 15, isImportField = "true_st", isWrap = false)
  private String agentName;

  @Excel(name = "代理结构", width = 20, isImportField = "true_st", isWrap = false)
  private String agentPath;

  @Excel(name = "代理层级", isImportField = "true_st", isWrap = false)
  private Integer levelNum;

  @Excel(
      name = "账号状态",
      replace = {"停用_0", "正常_1"},
      isImportField = "true_st",
      isWrap = false)
  private Integer accountStatus;

  @Excel(
      name = "下级会员",
      isImportField = "true_st",
      isStatistics = true,
      isWrap = false,
      numFormat = "0")
  private Integer subMember;

  @Excel(
      name = "有效会员",
      isImportField = "true_st",
      isStatistics = true,
      isWrap = false,
      numFormat = "0")
  private Integer efficientMember;

  @Excel(
      name = "下级代理",
      isImportField = "true_st",
      isStatistics = true,
      isWrap = false,
      numFormat = "0")
  private Integer subAgent;

  @Excel(
      name = "有效代理",
      isImportField = "true_st",
      isStatistics = true,
      isWrap = false,
      numFormat = "0")
  private String efficientAgent;

  @Excel(name = "方案名称", width = 15, isImportField = "true_st", isWrap = false)
  private String planName;

  @Excel(name = "方案等级", width = 15, isImportField = "true_st", isWrap = false)
  private String rebateLevel;

  @Excel(
      name = "公司总输赢",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal gameWin;

  @Excel(
      name = "下级会员佣金",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal memberCommission;

  @Excel(name = "下级会员佣金比例", isImportField = "true_st", numFormat = "#.##%", isWrap = false)
  private BigDecimal memberProportion;

  @Excel(
      name = "下级代理佣金",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal agentCommission;

  @Excel(name = "下级代理佣金比例", isImportField = "true_st", numFormat = "#.##%", isWrap = false)
  private BigDecimal agentProportion;

  @Excel(
      name = "流水返利",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal turnoverRebate;

  @Excel(name = "流水返利比例", isImportField = "true_st", numFormat = "#.##%", isWrap = false)
  private BigDecimal turnoverProportion;

  @Excel(
      name = "公司成本",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal totalCost;

  @Excel(
      name = "累计负盈利",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal negativeProfit;

  @Excel(
      name = "佣金调整",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal adjustmentAmount;

  @Excel(
      name = "实际佣金",
      width = 15,
      isImportField = "true_st",
      isStatistics = true,
      numFormat = "0.00",
      isWrap = false)
  private BigDecimal actualCommission;

  @Excel(
      name = "结算状态",
      replace = {"未结算_0", "风控审核_1", "财务审核_2", "已结算_3"},
      isImportField = "true_st",
      isWrap = false)
  private Integer status;

  @Excel(name = "创建时间", width = 20, isImportField = "true_st", isWrap = false)
  private String createTime;

  @Excel(name = "创建人", width = 15, isImportField = "true_st", isWrap = false)
  private String createBy;

  @Excel(name = "更新时间", width = 20, isImportField = "true_st", isWrap = false)
  private String updateTime;

  @Excel(name = "更新人", width = 15, isImportField = "true_st", isWrap = false)
  private String updateBy;

  @Excel(name = "备注", width = 15, isImportField = "true_st", isWrap = false)
  private String remark;
}
