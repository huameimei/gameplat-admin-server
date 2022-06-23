package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付排行榜数据结果
 *
 * @Author zak
 * @Date 2022/01/18 21:20:00
 */
@Data
public class PayLeaderboard {
  @Schema(description = "三方接口名称")
  private String interfaceName;

  @Schema(description = "三方接口名称")
  private String interfaceCode;

  @Schema(description = "使用平台数")
  private Integer interfaceUseNum;

  @Schema(description = "自动成功率")
  private BigDecimal autoSuccessRate;

  @Schema(description = "自动成功订单数")
  private Integer autoSuccessOrderNum;

  @Schema(description = "成功单数")
  private Integer successOrderNum;

  @Schema(description = "总单数")
  private Integer totalOrderNum;

  @Schema(description = "自动金额")
  private String autoSuccessAmount;

  @Schema(description = "总金额")
  private Integer totalAmount;
}
