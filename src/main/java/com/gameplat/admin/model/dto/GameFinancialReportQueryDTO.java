package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/3/6 18:07
 * @desc
 */
@Data
public class GameFinancialReportQueryDTO implements Serializable {

  @ApiModelProperty(value = "统计月份")
  private String statisticsTime;

  @ApiModelProperty(value = "游戏大类编码")
  private String gameType;

  @ApiModelProperty(value = "游戏平台编码")
  private String platformCode;
}
