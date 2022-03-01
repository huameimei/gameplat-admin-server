package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb @Date 2022/2/13 18:48 @Version 1.0
 */
@Data
public class DepositReportDto implements Serializable {

  /** 用户账号 */
  private String username;

  /** 开始日期 */
  private String startTime;

  /** 结束日期 */
  private String endTime;

  /** 开始充值次数 */
  private String startRNum;

  /** 结束充值次数 */
  private Integer endRNum;

  /** 开始充值金额 */
  private Integer startRAmount;

  /** 结束充值金额 */
  private String endRAmount;

  /** 是否首充(Y 是) */
  private String isFristCharge;

  /** 开始提现次数 */
  private Integer startWNum;

  /** 结束提现次数 */
  private Integer endWNum;

  /** 开始提现金额 */
  private String startWAmount;

  /** 结束提现金额 */
  private String endWAmount;

  private String isYFristCharge = "Y";
}
