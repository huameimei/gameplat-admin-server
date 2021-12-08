package com.gameplat.admin.model.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class MemberLevelVO implements Serializable {

  private Long id;

  private String levelName;

  private Integer levelValue;

  private Integer totalRechTimes;

  private Integer totalRechAmount;

  private Integer memberNum;

  private Integer memberLockNum;

  private Integer dayOfWithdraw;

  private Integer locked;

  private Integer status;

  private Integer isWithdraw;

  private Integer isDefault;
}
