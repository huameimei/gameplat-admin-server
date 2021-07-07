package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class MemberInfoQueryDTO implements Serializable {

  /** 会员账号 */
  public String account;
  /** 多账号 */
  private List<String> accoutList;
  /** 账号模糊查询 */
  private boolean accountFuzzy;
  /** 真实名称 */
  private String fullName;
  /** 真实名称模糊查询 */
  private boolean fullNameFuzzy;
  /** 会员备注 */
  private String userMemo;
  /** 会员备注是否模糊查询 */
  private boolean memoFuzzy;
  /** 会员类型 */
  private String userType;
  /** 账号状态(0-- 停用，1-- 正常，2 --- 冻结) */
  private Integer status;

  /** 代理账号 */
  private String agentAccount;

  /** 代理等级 */
  private Integer userLevel;
  /** 代理等级 */
  private Integer minUserLevel;

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
  private String weixin;
  /** 所属代理ID */
  private Long superId;

  /** 推广码 */
  private String intrCode;

  /** 注册IP */
  private String regIp;
  /** 注册ip模糊匹配 */
  private boolean regIpFuzzy;

  /** 登录IP */
  private String loginIp;
  /** 注册ip模糊匹配 */
  private boolean loginIpFuzzy;
  /** 余额大于多少money */
  private Double balance;
  /** 注册时间范围 */
  private Date regDateFrom;

  private Date regDateTo;
  /** 排序方式 */
  private String orderBy;
  /** 升序 or 降序 */
  private String sort;
  /** 银行卡号 */
  private String cardNo;

  /** 是否为代理 */
  private Integer isDl;
  /** 账户余额范围 */
  private Double moneyFrom;

  private Double moneyTo;

  /** 会员积分范围 */
  private Double pointsFrom;

  private Double pointsTo;

  /** 登录时间开始 */
  private Date loginDateFrom;
  /** 登录时间结束 */
  private Date loginDateTo;
  /** 登录时间之前 */
  private Date loginDateBefore;
  /** 登录时间之后 */
  private Date loginDateAfter;

  /** 实际取现总金额 */
  private Double uwMoney;

  /** 最近充值时间 */
  private Date rechTimeBefore;

  private Date rechTimeAfter;
  /** 充值次数范围 */
  private Integer rechCount;

  private Integer rechCountTo;
  /** 充值金额范围 */
  private Double rechMoneyFrom;

  private Double rechMoneyTo;

  /** 最近提现时间 */
  private Date uwTimeBefore;

  private Date uwTimeAfter;

  /** 取款次数 */
  private Integer uwCountFrom;

  private Integer uwCountTo;

  /** 银行地址 */
  private String bankAddress;
  /** 银行地址 是否模糊 */
  private boolean fuzzyBankAddress;
  /** 用户成长等级 */
  private String growthLevel;
}
