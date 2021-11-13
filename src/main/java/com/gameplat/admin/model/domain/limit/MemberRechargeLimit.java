package com.gameplat.admin.model.domain.limit;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberRechargeLimit implements Serializable {

  /** 会员入款提示信息 */
  private String rechargeTip;

  /** 已处理出入库订单是否允许其他账户操作 */
  private Integer isHandledAllowOthersOperate;

  /**
   * 同一收款账户 + 相同金额的充值
   */
  private Integer disableTransferIfHasUnhandled;

  /**
   * 会员最多允许未完成转账订单的数量
   */
  private Integer transferUnFinishedMaxCount;

  /**
   * 会员最多允许未完成在线支付的订单数量
   */
  private Integer onlinePayUnFinishedMaxCount;

  /**
   * 恶意刷单停用开关 1:开启，0:关闭
   */
  private Integer  maliceOnlinePayLimitSwitch;

  /**
   * 恶意刷单停用笔数
   */
  private Integer  maliceOnlinePayLimitCount;

  /**
   * 充值是否需要绑定提款方式
   */
  private Integer isBindingBank;

  /**
   * 充值优惠开关
   */
  private Integer manualRechargediscount;

  /**
   * 充值优惠模式
   */
  private Integer manualRechargediscountAmount;

  /**
   * 充值优惠数值
   */
  private double  manualRechargediscountValue;

}
