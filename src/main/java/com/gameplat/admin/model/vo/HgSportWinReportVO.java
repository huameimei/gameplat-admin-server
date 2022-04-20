package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/2/6 19:12
 * @desc
 */
@Data
public class HgSportWinReportVO implements Serializable {

  @ApiModelProperty(value = "投注类型")
  private String betType;

  @ApiModelProperty(value = "投注人数")
  private Long userCount = 0L;

  @ApiModelProperty(value = "投注次数")
  private Long betCount = 0L;

  @ApiModelProperty(value = "投注金额")
  private BigDecimal betAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "有效投注金额")
  private BigDecimal validAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "派奖金额")
  private BigDecimal sendPrizeAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "人均投注金额")
  private BigDecimal averageBetAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "游戏输赢金额")
  private BigDecimal gameWinAmount = BigDecimal.ZERO;

}
