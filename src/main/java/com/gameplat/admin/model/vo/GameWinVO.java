package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Author aguai @Description 游戏汇总报表 @Date 2022-01-26 */
@Data
public class GameWinVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "统计月份")
  private String countMonth;

  @ApiModelProperty(value = "总计游戏盈利")
  private BigDecimal totalWinAmount;

  @ApiModelProperty(value = "总计有效投注")
  private BigDecimal totalValidAmount;
}
