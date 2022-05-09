package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/3/23
 */
@Data
public class WithdrawChargeVO implements Serializable {

  /**
   * 未受理出款订单
   */
  private long withdrawCount;

  /**
   * 未受理充值订单(转账)
   */
  private long rechargeCount;

  /**
   * 额度补发订单
   */
  private long warningCount;
}
