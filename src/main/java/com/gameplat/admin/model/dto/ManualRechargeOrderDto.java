package com.gameplat.admin.model.dto;

import com.gameplat.base.common.json.JsonUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/** 人工入款，数据封装对象 */
@Data
public class ManualRechargeOrderDto implements Serializable {


  @NotNull private Long memberId; // 会员ID

  /**
   *充值金额
   */
  private BigDecimal amount;

  /**
   * 常态代码量
   */
  private BigDecimal normalDml;

  /**
   * 会员备注信息
   */
  private String remarks;

  private String account;
  /**
   * 计算积分标识
   */
  private Integer pointFlag;

  /**
   * 计算打码量标识
   */
  private Integer dmlFlag;

  /**
   * 优惠类型
   */
  private Integer discountType;

  /**
   *  优惠金额
   */
  private BigDecimal discountAmount;

  /**
   * 优惠打码量
   */
  private BigDecimal discountDml;

  /**
   * 是否直接入款
   */
  private boolean skipAuditing;

  /**
   *  审核备注信息
   */
  private String auditRemarks;

  /**
   * vip 等级
   */
  private String vip;

  /**
   * 会员层级
   */
  private String level;

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
