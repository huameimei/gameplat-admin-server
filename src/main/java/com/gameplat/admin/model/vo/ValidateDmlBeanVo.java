package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ValidateDmlBeanVo implements Serializable {

  private static final long serialVersionUID = 7033215796225338417L;

  private String usename;
  private List<ValidWithdrawVO> rows;
  /**
   * 要求打码量
   */
  private BigDecimal requireDML;
  /**
   * 放宽额度
   */
  private BigDecimal relaxQuota;
  /**
   * 有效投注额
   */
  private BigDecimal sumAllDml;
  /**
   * 总打码量 通过-true，不通过false
   */
  private boolean allDmlPass;
  /**
   *
   提现需打码金额
   */
  private BigDecimal remainRequiredDml;
  /**
   * 需要扣除金额
   */
  private BigDecimal sumAllDeduct;
  private BigDecimal yetWithdraw;


}
