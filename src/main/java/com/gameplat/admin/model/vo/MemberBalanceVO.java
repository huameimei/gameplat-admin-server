package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberBalanceVO implements Serializable {

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

  /** 用户余额 */
  private BigDecimal balance;

  /** 会员VIP等级 */
  private Integer vipLevel;

  /** 上级代理 */
  private String parentName;

  private long id;
}
