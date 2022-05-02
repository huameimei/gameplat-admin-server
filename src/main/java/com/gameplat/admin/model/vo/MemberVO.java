package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lily
 */
@Data
public class MemberVO implements Serializable {

  private Long id;

  /** 会员账号 */
  @Excel(name = "会员账号", width = 20, isImportField = "true_st")
  private String account;

  /** 会员昵称 */
  @Excel(name = "会员昵称", width = 20, isImportField = "true_st")
  private String nickname;

  /** 真实姓名 */
  @Excel(name = "真实姓名", width = 15, isImportField = "true_st")
  private String realName;

  @Excel(name = "VIP等级", width = 8, isImportField = "true_st")
  @ApiModelProperty(value = "VIP等级")
  private Integer vipLevel;

  /** 会员备注 */
  @Excel(name = "会员备注", width = 20, isImportField = "true_st")
  private String remark;

  private Long parentId;

  /** 上级代理 */
  @Excel(name = "上级代理", width = 12, isImportField = "true_st")
  private String parentName;

  /** 用户类型 */
  @Excel(
      name = "会员类型",
      replace = {"会员_M", "代理_A", "推广_P", "试玩_T"},
      width = 10,
      isImportField = "true_st")
  private String userType;

  /** 会员层级 */
  @Excel(name = "会员层级", width = 11, isImportField = "true_st")
  private Integer userLevel;

  /** 下级人数 */
  @Excel(name = "下级人数", width = 10, isImportField = "true_st")
  private String lowerNum;

  @Excel(
      name = "提现状态",
      replace = {"启用_Y", "禁用_N"},
      width = 10,
      isImportField = "true_st")
  private String withdrawFlag;

  /** 账户余额 单位分 */
  @Excel(name = "账户余额", width = 14, isImportField = "true_st")
  private BigDecimal balance;

  /** 余宝金额 */
  @Excel(name = "提现冻结", width = 14, isImportField = "true_st")
  private BigDecimal freeze;

  /** 累计充值金额 */
  @Excel(name = "充值金额", width = 14, isImportField = "true_st")
  private BigDecimal totalRechAmount;

  /** 累计充值次数 */
  @Excel(name = "充值次数", width = 10, isImportField = "true_st")
  private Integer totalRechTimes;

  /** 累计出款金额 */
  @Excel(name = "提现金额", width = 14, isImportField = "true_st")
  private BigDecimal totalWithdrawAmount;

  /** 累计出款次数 */
  @Excel(name = "提现次数", width = 10, isImportField = "true_st")
  private Integer totalWithdrawTimes;

  @Excel(name = "注册IP", width = 22, isImportField = "true_st")
  private String registerIp;

  /** 注册时间 */
  @Excel(name = "注册时间", width = 21, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  private String dialCode;

  /** 邀请码 */
  @Excel(name = "推广码", width = 16, isImportField = "true_st")
  private String invitationCode;

  @Excel(name = "注册域名", width = 21, isImportField = "true_st")
  private String registerHost;

  /** 会员状态 */
  @Excel(
      name = "账户状态",
      replace = {"禁用_-1", "停用_0", "正常_1"},
      width = 10,
      isImportField = "true_st")
  private Integer status;

  /** 是否在线 */
  private Boolean online;

  /** 最近登录IP */
  @Excel(name = "最后登录IP", width = 17, isImportField = "true_st")
  private String lastLoginIp;

  /** 最近登录时间 */
  @Excel(
      name = "最后登录时间",
      width = 21,
      isImportField = "true_st",
      exportFormat = "yyyy-MM-dd HH:mm:ss")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date lastLoginTime;

  /** 代理路径 */
  private String superPath;

  /** 代理等级 */
  private String agentLevel;

  /** 注册来源 */
  private Integer registerSource;

  private String registerBrowser;

  private String registerOs;

  @ApiModelProperty(value = "当前会员成长值")
  private Integer growth;

  @ApiModelProperty(value = "玩家金币数")
  private Integer goldCoin;

  private Integer salaryFlag;

  /** 输赢金额 */
  private BigDecimal winAmount = BigDecimal.ZERO;

  /** 返水金额 */
  private BigDecimal waterAmount = BigDecimal.ZERO;
}
