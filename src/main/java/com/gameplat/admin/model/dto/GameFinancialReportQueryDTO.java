package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/3/6 18:07
 * @desc
 */
@Data
public class GameFinancialReportQueryDTO implements Serializable {

  @Schema(description = "统计月份")
  private String statisticsTime;

  @Schema(description = "游戏大类编码")
  private String gameType;

  @Schema(description = "游戏平台编码")
  private String platformCode;
}
