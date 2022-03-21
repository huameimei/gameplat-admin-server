package com.gameplat.admin.model.dto;

import lombok.Data;

@Data
public class DividePeriodsQueryDTO {
  private Long id;
  private Integer settleStatus; // 结算状态 1 未结算  2 已结算
  private Integer grantStatus; // 派发状态 1 未派发  2 已派发
  private Integer divideType; // 结算时业主开启的分红模式 1 固定比例  2 裂变  3 层层
}
