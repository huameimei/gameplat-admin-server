package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/18 19:36
 * @desc
 */
@Data
public class TotalVipWealTypeVO implements Serializable {

  @ApiModelProperty("派发金额")
  private BigDecimal rewordAmount;

  @ApiModelProperty("0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金 4：每月红包")
  private Integer type;
}
