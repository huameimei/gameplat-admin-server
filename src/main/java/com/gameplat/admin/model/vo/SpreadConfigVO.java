package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpreadConfigVO {

  private Long id;
  /** 代理Id */
  private Long agentId;

  /** 代理账号 */
  @Excel(name = "代理账号", height = 20, width = 30, isImportField = "true_st")
  private String agentAccount;

  /** 推广码 */
  @Excel(name = "推广码", height = 20, width = 30, isImportField = "true_st")
  private String code;

  /** 推广地址 */
  @Excel(name = "推广地址", height = 20, width = 30, isImportField = "true_st")
  private String externalUrl;

  /** 推广类型 */
  @Excel(name = "推广页面", height = 20, width = 30, isImportField = "true_st")
  private Integer spreadType;

  /** 是否专属类型 */
  private Integer exclusiveFlag;

  /** 推广用户类型 */
  @Excel(
      name = "会员类型",
      replace = {"会员_0", "代理_1"},
      height = 20,
      width = 30,
      isImportField = "true_st")
  private Integer userType;

  /** 用户层级 */
  @Excel(name = "会员层级", height = 20, width = 30, isImportField = "true_st")
  private Integer userLevel;

  /** 有效天数 */
  @Excel(name = "有效天数", height = 20, width = 30, isImportField = "true_st")
  private Integer effectiveDays;

  /** 访问数 */
  @Excel(name = "访问统计", height = 20, width = 30, isImportField = "true_st")
  private Integer visitCount;

  /** 注册数 */
  @Excel(
      name = "状态",
      replace = {"启用_1", "未启用_0"},
      height = 20,
      width = 30,
      isImportField = "true_st")
  private Integer registCount;

  /** 彩票反水比率 */
  private double rebate;

  /** 注册送彩金 */
  private BigDecimal discountAmount;

  /** 状态 */
  private Integer status;

  /** 是否开启层层代分红预设 */
  private Integer isOpenDividePreset;

  /** 分红预设配置 */
  private String divideConfig;

  @Excel(name = "创建时间", height = 20, width = 30, isImportField = "true_st")
  private Date createTime;

  private Date updateTime;
}
