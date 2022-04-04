package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/6 17:28
 * @desc 游戏财务报表
 */
@Data
public class TotalGameFinancialReportVO implements Serializable {

  @ApiModelProperty(value = "有效投注额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "上月输赢金额")
  private BigDecimal lastWinAmount;

  @ApiModelProperty(value = "累计输赢金额")
  private BigDecimal accumulateWinAmount;
}
