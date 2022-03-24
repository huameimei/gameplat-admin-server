package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 平台费 @Author : cc @Date : 2022/3/23 */
@Data
public class PlatformFeeVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "统计月份")
  private String countMonth;

  @ApiModelProperty(value = "游戏名称")
  private String gameName;

  @ApiModelProperty(value = "游戏编码")
  private String gameCode;

  @ApiModelProperty(value = "游戏盈利")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "有效投注")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "收费比例")
  private BigDecimal reportRate;

  @ApiModelProperty(value = "游戏费用")
  private BigDecimal gameFee;
}
