package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class MemberInfoVO implements Serializable {

  private Long id;

  /** 会员账号 */
  private String account;

  /** 会员昵称 */
  private String nickname;

  /** 真实姓名 */
  private String realName;

  /** 会员层级/代理等级 */
  private Integer userLevel;

  /** 用户类型 */
  private String userType;

  /** 会员状态 禁用：-1；停用：0；正常：1*/
  private Integer status;

  private String withdrawFlag;

  private Character sex;

  private String qq;

  private String email;

  private String phone;

  private String dialCode;

  private String wechat;

  private String birthday;

  private Integer parentId;
  /** 上级代理 */
  private String parentName;

  /** 邀请码 */
  private String invitationCode;

  /** 账户余额*/
  private BigDecimal balance;

  private String rebate;

  /** 累计充值金额 */
  private BigDecimal totalRechAmount;

  /** 累计充值次数 */
  private Integer totalRechTimes;

  /** 累计出款金额 */
  private BigDecimal totalWithdrawAmount;

  /** 累计出款次数 */
  private Integer totalWithdrawTimes;

  private Date lastRechTime;

  private Date lastWithdrawTime;

  private String language;

  private String registerIp;

  private String registerHost;

  /** 注册来源 */
  private Integer registerSource;

  private String registerBrowser;

  private String registerOs;

  /** 注册时间 */
  private Date createTime;

  /** 最近登录时间 */
  private Date lastLoginTime;

  /** 最近登录IP */
  private String lastLoginIp;

  /** 会员备注 */
  private String remark;

  /** 会员VIP等级 */
  private Integer level;

  private Integer growth;
  /** 代理路径 */
  private String superPath;

  private String agentLevel;

  private Integer tableIndex;
}
