package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dozer.Mapping;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 游戏汇总报表 @Author : cc @Date : 2022/3/23 */
@Data
public class CompanyCostVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计月份")
  private String countMonth;

  @Schema(description = "项目")
  @Mapping(value = "gameName")
  private String itemName;

  @Schema(description = "游戏输赢")
  @Mapping(value = "winAmount")
  private BigDecimal itemA;

  @Schema(description = "收费比例")
  @Mapping(value = "reportRate")
  private BigDecimal itemB;

  @Schema(description = "成本费用")
  @Mapping(value = "gameFee")
  private BigDecimal fee;
}
