package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberLevelAddDTO implements Serializable {

  @NotBlank(message = "层级名称不能为空")
  private String levelName;

  @NotNull(message = "层级值不能为空")
  private Integer levelValue;

  @Range(min = 0, max = 9999999, message = "充值总次数阀值范围必须在0~9999999之间")
  private Integer rechargeTotalTime;

  @Range(min = 0, max = 9999999, message = "充值总金额阀值范围必须在0~9999999之间")
  private Integer rechargeAmountTotal;

  @Range(min = 0, max = 9999999, message = "每日提现次数阀值范围必须在0~9999999之间")
  private Integer dayOfWithdraw;
}
