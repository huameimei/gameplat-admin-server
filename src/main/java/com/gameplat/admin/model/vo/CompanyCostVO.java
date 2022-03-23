package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 游戏汇总报表 @Author : cc @Date : 2022/3/23 */
@Data
public class CompanyCostVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "统计月份")
  private String countMonth;

  @ApiModelProperty(value = "项目")
  @Mapping(value = "gameName")
  private String itemName;

  @ApiModelProperty(value = "游戏输赢")
  @Mapping(value = "winAmount")
  private BigDecimal itemA;

  @ApiModelProperty(value = "收费比例")
  @Mapping(value = "reportRate")
  private BigDecimal itemB;

  @ApiModelProperty(value = "成本费用")
  @Mapping(value = "gameFee")
  private BigDecimal fee;
}
