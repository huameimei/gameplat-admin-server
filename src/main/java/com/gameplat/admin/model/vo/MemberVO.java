package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberVO implements Serializable {

  private Long id;

  /** 会员账号 */
  private String account;

  /** 会员昵称 */
  private String nickname;

  /** 真实姓名 */
  private String realName;

  /** 会员层级 */
  private Integer userLevel;

  /** 代理等级 */
  private String agentLevel;

  /** 代理路径 */
  private String superPath;

  /** 用户类型 */
  private String userType;

  /** 上级代理 */
  private String parentName;

  private String dialCode;

  private String phone;

  private String email;

  private String wechat;

  private String qq;

  private String withdrawFlag;

  /** 邀请码 */
  private String invitationCode;

  /** 下级人数 */
  private String lowerNum;

  /** 账户余额 单位分 */
  private BigDecimal balance;

  /** 余宝金额 */
  private BigDecimal freeze;

  /** 累计充值金额 */
  private BigDecimal totalRechAmount;

  /** 累计充值次数 */
  private Integer totalRechTimes;

  /** 累计出款金额 */
  private BigDecimal totalWithdrawAmount;

  /** 累计出款次数 */
  private Integer totalWithdrawTimes;

  /** 注册时间 */
  private Date createTime;

  private String registerIp;

  private String registerHost;

  /** 注册来源 */
  private Integer registerSource;

  private String registerBrowser;

  private String registerOs;

  /** 会员状态 */
  private Integer status;

  /** 是否在线 */
  private Integer online;

  /** 最近登录时间 */
  private Date lastLoginTime;

  /** 最近登录IP */
  private String lastLoginIp;

  /** 会员备注 */
  private String remark;

  @ApiModelProperty(value = "当前会员等级")
  private Integer level;

  @ApiModelProperty(value = "当前会员成长值")
  private Integer growth;

}
