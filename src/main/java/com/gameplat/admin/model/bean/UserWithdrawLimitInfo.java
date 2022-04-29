package com.gameplat.admin.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** 提款限额配置 */
@Data
public class UserWithdrawLimitInfo implements Serializable {

  /** 会员第几次提现 */
  private Long timesForWithdrawal;

  /** 单笔提现最低金额 */
  private BigDecimal minAmountPerOrder;

  /** 单笔提现最高金额 */
  private BigDecimal maxAmountPerOrder;

  /** 单日提现最高金额上限 */
  private BigDecimal dayMaxAmount;
}
