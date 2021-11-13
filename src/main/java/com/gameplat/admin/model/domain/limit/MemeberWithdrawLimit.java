package com.gameplat.admin.model.domain.limit;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户提现限制配置
 */
@Data
public class MemeberWithdrawLimit implements Serializable {

  /**
   * 是否需要验证码 0--启用 1--禁用
   */
  private Integer cashVerification = 0;

  /**
   * 会员申请取款取款密码输入错误限制次数
   */
  private Integer withdrawalPasswordErrorTriesLimit;

  /**
   * 两次提现时间间隔
   */
  private Integer twiceCashTimeInterval;

  /**
   * 提现被驳回后再次提现的时间间隔
   */
  private Integer outOfCourtAgainCashTimeInterval;

  /**
   * 单笔提现金额上限
   */
  private Double singleCashMoneyUpperLimit;

  /**
   * 单笔提现金额下限
   */
  private Double singleCashMoneyLowerLimit;

  /**
   * 单日提现金额上限
   */
  private Double oddDaysCashMoneyUpperLimit;

  /**
   * 会员提现打码量比例
   */
  private Integer dmlRate;

  /**
   * 是否扣除行政费用
   */
  private Integer whetherDeductAdministrationCost;

  /**
   * 行政费用扣除固定金额
   */
  private Double fixedBalance;

  /**
   * 行政费用扣除充值金额比例
   */
  private Double administrationCostDeductPayMoneyRatio;


  /**
   * 单日提现次数上限
   */
  private Integer oddDaysCashTriesLimit;

  /**
   * 超过当日提现次数是否允许提现 0允许,1禁止
   */
  private Integer cashNumberExceedWhetherAllowCash = 0;

  /**
   * 超过当日最大次数收手续费类型，0定额，1百分比
   */
  private Integer counterFeeMode;

  /**
   * 超过当日最大次数要收手续费
   */
  private  Double  exceedCashNumberUpperLimitServiceCharge;

  /**
   * 提现银行详细地址 0可见,1必填 2隐藏
   */
  private Integer cashBankDetailedAddress;

  /**
   * 重复提现限制 0否,1是
   */
  private Integer repetitionCashLimit;

  /**
   * 会员申请出款后提示信息
   */
  private String userApplyLoanAfterHintsMessage;

}
