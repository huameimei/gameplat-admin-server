package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** 每个一级游戏编码对应的分红对象 @Author : cc @Date : 2021/12/23 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDivideVo {

  @Schema(description = "游戏大类")
  private String liveGameName;

  @Schema(description = "游戏大类编码")
  private String liveGameCode;

  @Schema(description = "一级游戏名称")
  private String name;

  @Schema(description = "一级游戏编码")
  private String code;

  @Schema(description = "金额比例")
  private BigDecimal amountRatio;

  @Schema(description = "上级分配给用户的分红比例")
  private BigDecimal divideRatio;

  @Schema(description = "计算分红金额时直接上级的分红计算比例")
  private BigDecimal parentDivideRatio;

  @Schema(description = "结算方式1-输赢 2-投注额")
  private Integer settleType;

  @Schema(description = "上级分配的分红比例可调整的最大值")
  private BigDecimal maxRatio;

  @Schema(description = "上级分配的分红比例可调整的最小值")
  private BigDecimal minRatio;
}
