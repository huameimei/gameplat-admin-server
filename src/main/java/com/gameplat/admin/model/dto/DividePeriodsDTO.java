package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description : 分红期数 @Author : cc @Date : 2022/2/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DividePeriodsDTO {
  private Long id;

  @Schema(description = "期数结算起始日期")
  private String startDate;

  @Schema(description = "期数结算截止日期")
  private String endDate;

  @Schema(description = "结算状态 1 未结算  2 已结算")
  private Integer settleStatus;

  @Schema(description = "派发状态 1 未派发  2 已派发 3 已回收")
  private Integer grantStatus;

  @Schema(description = "结算时业主开启的分红模式 1 固定比例  2 裂变  3 层层代 4 平级")
  private Integer divideType;
}
