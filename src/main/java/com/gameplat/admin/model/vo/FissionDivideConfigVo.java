package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description : 每个一级游戏编码对应的分红对象 @Author : cc @Date : 2021/12/23
 */
@Data
public class FissionDivideConfigVo {
  @Schema(description = "游戏配置名称")
  private String liveGameName;

  @Schema(description = "游戏配置code")
  private String liveGameCode;

  @Schema(description = "游戏名称")
  private String name;

  @Schema(description = "游戏code")
  private String code;

  @Schema(description = "金额比例")
  private BigDecimal amountRatio;

  @Schema(description = "结算方式1-输赢 2-投注额")
  private Integer settleType;
}
