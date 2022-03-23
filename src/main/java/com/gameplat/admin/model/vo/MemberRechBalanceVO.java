package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberRechBalanceVO implements Serializable {


  /** 会员账号 */
  private String account;


  /** 彩金 */
  private BigDecimal amount;

  /**
   * 会员备注
   */
  private String remark;


  /** 打码量倍数 */
  private BigDecimal betMultiple;



  /** 审核备注 */
  private String auditRemarks;


  public MemberRechBalanceVO() {
    this.amount = BigDecimal.ZERO;
    this.betMultiple = BigDecimal.ZERO;
  }
}
