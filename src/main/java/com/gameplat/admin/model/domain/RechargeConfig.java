package com.gameplat.admin.model.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;


@Data
@TableName("recharge_config")
public class RechargeConfig implements Serializable {

  @TableId
  private Long id;

  /**
   * 充值方式 1 - 转账汇款, 2 - 在线支付, 3 - 后台入款
   */
  @NotNull
  private Integer mode;

  // 支付类型编码
  private String payType;

  // 单笔最高限额
  @NotNull
  @Min(0)
  private BigDecimal maxAmount;

  // 单笔最底限额
  @NotNull
  @Min(0)
  private BigDecimal minAmount;

  /**
   * 充值优惠触发模式 0 - 从不, 1- 每次充值, 2 - 首充
   */
  @NotNull
  private Integer rechargeDiscountTrigger;

  /**
   * 充值优惠计算模式: 0 - 百分比, 1 - 定额
   */
  private Integer rechargeDiscountMode;

  // 充值优惠计算值
  @Min(0)
  private BigDecimal rechargeDiscountValue;

  // 充值优惠上限
  @Min(0)
  private BigDecimal rechargeDiscountMaxAmount;

  // 其它优惠上限
  @Min(0)
  private BigDecimal otherDiscountMaxAmount;

  // 常态打码量比率
  @Min(0)
  private BigDecimal normalDmlRate;

  // 优惠打码量倍数
  @Min(0)
  private BigDecimal discountDmlMultiple;

  // 充值间隔
  @Min(0)
  private Long minPeriod;

  /**
   * 是否启用验证码 0 - 否, 1 - 是
   */
  @NotNull
  private Integer validateCodeEnabled;

  /**
   * 取消入款后是否限制入款 0 - 否, 1 - 是
   */
  private Integer disableAfterCancelled;

  // 风控金额
  private String riskMoney;

  //会员入款提示
  private String rechargeTip;

  //入款备注颜色
  private String rechargeRemarkColor;

  //前台扫码转账文字描述
  private String scanRemark;

  /**
   * 会员充值金额输入框提示信息
   */
  private String rechargeInputTip;

  //充值是否需要绑定银行卡，0-不需要，1-需要
  private Integer isBindingBank;

  // 每笔充值优惠开关
  private Integer eachRechargeDiscountDisabled;

  /**
   * 每笔充值优惠计算模式: 0 - 百分比, 1 - 定额
   */
  private Integer eachRechargeDiscountMode;

  // 每笔充值优惠计算值
  @Min(0)
  private BigDecimal eachRechargeDiscountValue;

  // 每笔充值优惠上限
  @Min(0)
  private BigDecimal eachRechargeDiscountMaxAmount;

  // 每笔充值优惠开关
  private Integer firstRechargeDiscountDisabled;

  /**
   * 首充充值优惠计算模式: 0 - 百分比, 1 - 定额
   */
  private Integer firstRechargeDiscountMode;

  // 首充充值优惠计算值
  @Min(0)
  private BigDecimal firstRechargeDiscountValue;

  // 首充充值优惠上限
  @Min(0)
  private BigDecimal firstRechargeDiscountMaxAmount;

}
