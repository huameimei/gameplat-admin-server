package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameProxyDataVo implements Serializable {

  @Schema(description = "代理总计")
  private BigDecimal allDivideAmount;

  @Schema(description = "代理分红")
  private BigDecimal divideAmount;

  @Schema(description = "代理分红人数")
  private int divideNum;

  @Schema(description = "代理日工资")
  private BigDecimal salaryGrant;

  @Schema(description = "代理日工资人数")
  private int salaryNum;

  @Schema(description = "代理返点")
  private BigDecimal proxyWaterAmount;

  @Schema(description = "代理返点人数")
  private int proxyWaterNum;

  public GameProxyDataVo() {
    this.allDivideAmount = BigDecimal.ZERO;
    this.divideAmount = BigDecimal.ZERO;
    this.salaryGrant = BigDecimal.ZERO;
    this.proxyWaterAmount = BigDecimal.ZERO;
  }

  public BigDecimal getAllDivideAmount() {
    return divideAmount.add(salaryGrant).add(proxyWaterAmount);
  }
}
