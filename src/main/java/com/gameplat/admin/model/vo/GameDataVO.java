package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/12 22:20
 * @desc
 */
@Data
public class GameDataVO implements Serializable {

  @ApiModelProperty(value = "一级游戏编码")
  private String gameKind;

  @ApiModelProperty(value = "有效投注额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "累计输赢金额")
  private BigDecimal accumulateWinAmount;
}
