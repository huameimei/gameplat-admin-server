package com.gameplat.admin.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MemberRechBalanceVO implements Serializable {


  /** 会员账号 */
  @ExcelProperty(index = 0, value = "会员账号")
  private String account;


  /** 彩金 */
  @ExcelProperty(index = 1, value = "彩金")
  private BigDecimal amount;

  /**
   * 会员备注
   */
  @ExcelProperty(index = 2, value = "会员备注")
  private String remark;


  /** 打码量倍数 */
  @ExcelProperty(index = 3, value = "打码量倍数")
  private BigDecimal betMultiple;



  /** 审核备注 */
  @ExcelProperty(index = 4, value = "审核备注")
  private String auditRemarks;


  public MemberRechBalanceVO() {
    this.amount = BigDecimal.ZERO;
    this.betMultiple = BigDecimal.ZERO;
  }
}
