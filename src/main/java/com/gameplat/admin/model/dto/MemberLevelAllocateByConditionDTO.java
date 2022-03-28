package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberLevelAllocateByConditionDTO implements Serializable {

  @NotNull(message = "代理账号不能为空")
  private String parentNames;

  private String levelValues;

  private Integer maxRechargeNum;

  private Integer minRechargeNum;

  private Long maxRechargeAmount;

  private Long minRechargeAmount;

  private String lastRechargeTime;

  private String userType;

  @NotNull(message = "是否只查询直属下级不能为空")
  private Boolean subordinateOnly;

  @NotNull(message = "是否包含自己不能为空")
  private Boolean itself;

  @NotNull(message = "转至层级值不能为空")
  private Integer levelValue;
}
