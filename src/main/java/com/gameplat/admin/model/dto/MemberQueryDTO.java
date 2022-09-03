package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MemberQueryDTO implements Serializable {

  /** 会员账号 */
  private String account;

  private Long id;

  /** 多账号 */
  private List<String> accountList;

  /** 账号模糊查询 */
  private Boolean accountFuzzy;

  /** 真实名称 */
  private String realName;

  /** 真实名称模糊查询 */
  private Boolean realNameFuzzy;

  /** 会员备注 */
  private String remark;

  /** 会员备注是否模糊查询 */
  private Boolean remarkFuzzy;

  /** 会员类型 */
  private String userType;

  /** 账号状态(0-- 停用，1-- 正常，2 --- 冻结) */
  private Integer status;

  /** 代理账号 只查看下级时 有值 */
  private String agentAccount;

  /** 仅查询直属下级 */
  private Boolean subordinateOnly;

  /** 权限控制 账号可以查看的会员层级 */
  private List<String> levelList;

  /** 会员层级 */
  private Integer userLevel;

  /** 代理层级 */
  private Integer agentLevel;

  private Integer maxUserLevel;

  /** 充值层级 */
  private String rechLevel;

  /** 会员模式 */
  private Integer userMode;

  /** 昵称 */
  private String nickname;

  /** 手机号 */
  private String phone;

  /** 邮箱 */
  private String email;

  /** qq */
  private String qq;

  /** 微信 */
  private String wechat;

  /** 推广码 */
  private String invitationCode;

  /** 注册IP */
  private String registerIp;

  /** 注册ip模糊匹配 */
  private Boolean registerIpFuzzy;

  /** 登录IP */
  private String lastLoginIp;

  /** 注册ip模糊匹配 */
  private Boolean lastLoginIpFuzzy;

  private Long balanceFrom;

  private Long balanceTo;

  /** 注册时间范围 */
  private String registerTimeFrom;

  private String registerTimeTo;

  /** 登录时间开始 */
  private String lastLoginTimeFrom;

  /** 登录时间结束 */
  private String lastLoginTimeTo;

  /** 多少天内登陆过 */
  private Integer dayOfLogin;

  /** 多少天内未登陆 */
  private Integer dayOfNotLogin;

  private String lastRechTimeFrom;

  private String lastRechTimeTo;

  /** 充值次数范围 */
  private Integer rechTimesFrom;

  private Integer rechTimesTo;

  /** 充值金额范围 */
  private Long rechAmountFrom;

  private Long rechAmountTo;

  /** 最近提现时间 */
  private String lastWithdrawTimeFrom;

  private String lastWithdrawTimeTo;

  /** 累计提现次数 */
  private Integer withdrawTimesFrom;

  private Integer withdrawTimesTo;

  /** 累计提款金额 */
  private Long withdrawAmountFrom;

  /** 超过多少天未充值 */
  private Integer dayOfNoRecha;

  /** 未首冲 */
  private Boolean noFirstRech;

  /** 未首提 */
  private Boolean noFirstWithdraw;

  /** 未二次充值 */
  private Boolean notTwiceRech;

  private List levels;

  private String parentName;

  /** 是否包含代理自己 */
  private Boolean itself;

  /** 会员当前等级 */
  private Integer vipLevel;

  /**
   * 银行卡号
   */
  private String cardNo;

  /**
   * 模糊查询银行卡号
   */
  private Boolean cardNoFuzzy;

  /**
   * 虚拟币
   */
  private String virtualCard;

  private Boolean virtualCardFuzzy;

  /**
   * 开户行地址
   */
  private String address;

  /**
   * 开户行地址模糊查询
   */
  private Boolean addressFuzzy;

}
