package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberLevelEditDTO implements Serializable {

  @NotNull(message = "层级编号不能为空")
  private Long id;

  @Range(min = 0, max = 9999999, message = "充值总次数阀值范围必须在0~9999999之间")
  private Integer totalRechTimes;

  @Range(min = 0, max = 9999999, message = "充值总金额阀值范围必须在0~9999999之间")
  private Integer totalRechAmount;

  @Range(min = 0, max = 9999, message = "每日提现次数范围必须在0~9999之间")
  private Integer dayOfWithdraw;
}
