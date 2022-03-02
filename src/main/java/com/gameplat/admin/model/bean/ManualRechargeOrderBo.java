package com.gameplat.admin.model.bean;

import com.gameplat.base.common.json.JsonUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/** 人工入款，数据封装对象 */
@Data
public class ManualRechargeOrderBo {

  @NotNull private Long memberId; // 会员ID

  @NotNull private BigDecimal amount; // 充值金额

  private BigDecimal normalDml; // 常态代码量

  private String remarks; // 会员备注信息

  private String account; // 会员账号
  @NotNull private Integer pointFlag; // 计算积分标识

  @NotNull private Integer dmlFlag; // 计算打码量标识

  private Integer discountType; // 优惠类型

  private BigDecimal discountAmount; // 优惠金额

  private BigDecimal discountDml; // 优惠打码量

  private boolean skipAuditing; // 是否直接入款

  private String auditRemarks; // 审核备注信息

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
