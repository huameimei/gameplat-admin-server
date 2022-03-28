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

  private long withdrawCount;

  private long rechargeCount;

//    private Integer withdrawCounts;
}
